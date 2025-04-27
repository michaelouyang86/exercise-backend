package com.michael.exercise.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.michael.exercise.dtos.AddTeacherUnavailableDateRequest;
import com.michael.exercise.dtos.CreateTeacherRequest;
import com.michael.exercise.models.TeacherRecurringAvailability;
import com.michael.exercise.models.TeacherUnavailableDate;
import com.michael.exercise.models.AddTeacherExceptionAvailability;
import com.michael.exercise.models.AddTeacherRecurringAvailability;
import com.michael.exercise.models.ScheduledClass;
import com.michael.exercise.models.Teacher;
import com.michael.exercise.models.TeacherAvailability;
import com.michael.exercise.models.TeacherExceptionAvailability;
import com.michael.exercise.repositories.TeacherRepository;

import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.WeekFields;

@Service
@AllArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Teacher> getTeachers() {
        return teacherRepository.getTeachers();
    }

    public void createTeacher(CreateTeacherRequest request) {
        String password = request.getPhone().substring(7);
        teacherRepository.createTeacher(request, passwordEncoder.encode(password));
    }

    public void deleteTeacher(int id) {
        teacherRepository.deleteTeacher(id);
    }

    public void addTeacherRecurringAvailability(AddTeacherRecurringAvailability availability) {
        LocalTime startTime = availability.getStartTime();
        LocalTime endTime = availability.getEndTime();
        if (startTime.equals(endTime) || startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }
        teacherRepository.addTeacherRecurringAvailability(availability);
    }

    public List<TeacherRecurringAvailability> getTeacherRecurringAvailabilities(int teacherId) {
        return teacherRepository.getTeacherRecurringAvailabilities(teacherId);
    }

    public void deleteTeacherRecurringAvailability(int teacherId, int recurringId) {
        teacherRepository.deleteTeacherRecurringAvailability(teacherId, recurringId);
    }

    public void addTeacherExceptionAvailability(AddTeacherExceptionAvailability exceptionAvailability) {
        teacherRepository.addTeacherExceptionAvailability(exceptionAvailability);
    }

    public List<TeacherExceptionAvailability> getTeacherExceptionAvailabilities(int teacherId) {
        return teacherRepository.getTeacherExceptionAvailabilities(teacherId);
    }

    public void deleteTeacherExceptionAvailability(int teacherId, int exceptionId) {
        teacherRepository.deleteTeacherExceptionAvailability(teacherId, exceptionId);
    }

    public void addTeacherUnavailableDate(int teacherId, AddTeacherUnavailableDateRequest request) {
        teacherRepository.addTeacherUnavailableDate(teacherId, request);
    }

    public List<TeacherUnavailableDate> getTeacherUnavailableDates(int teacherId) {
        return teacherRepository.getTeacherUnavailableDates(teacherId);
    }

    public void deleteTeacherUnavailableDate(int teacherId, int unavailableDateId) {
        teacherRepository.deleteTeacherUnavailableDate(teacherId, unavailableDateId);
    }

    // Get the availabilities for a specific teacher for a specific week
    public List<TeacherAvailability> getTeacherAvailabilities(int teacherId, String isoWeek) {
        int year = Integer.parseInt(isoWeek.substring(0, 4));
        int week = Integer.parseInt(isoWeek.substring(6));
        LocalDate monday = LocalDate.now()
                                    .withYear(year)
                                    .with(WeekFields.ISO.weekOfWeekBasedYear(), week)
                                    .with(WeekFields.ISO.dayOfWeek(), 1);
        LocalDate tuesday = monday.plusDays(1);
        LocalDate wednesday = monday.plusDays(2);
        LocalDate thursday = monday.plusDays(3);
        LocalDate friday = monday.plusDays(4);
        LocalDate saturday = monday.plusDays(5);
        LocalDate sunday = monday.plusDays(6);

        // Create a list of TeacherAvailability for each day of the week
        List<TeacherAvailability> teacherAvailabilities = new ArrayList<>();
        teacherAvailabilities.add(getTeacherAvailability(teacherId, monday));
        teacherAvailabilities.add(getTeacherAvailability(teacherId, tuesday));
        teacherAvailabilities.add(getTeacherAvailability(teacherId, wednesday));
        teacherAvailabilities.add(getTeacherAvailability(teacherId, thursday));
        teacherAvailabilities.add(getTeacherAvailability(teacherId, friday));
        teacherAvailabilities.add(getTeacherAvailability(teacherId, saturday));
        teacherAvailabilities.add(getTeacherAvailability(teacherId, sunday));
        return teacherAvailabilities;
    }

    // Get the availability for a specific teacher on a specific date
    private TeacherAvailability getTeacherAvailability(int teacherId, LocalDate date) {
        TeacherAvailability availability = new TeacherAvailability();
        availability.setDate(date);

        String dayOfWeek = date.getDayOfWeek().name();
        availability.setDayOfWeek(dayOfWeek);

        // Check if the teacher is unavailable on this date
        boolean isUnavailable = teacherRepository.isUnavailable(teacherId, date);
        if (isUnavailable) {
            availability.setTimeslots(Collections.emptySet());
            return availability;
        }

        Set<LocalTime> timeslots = new LinkedHashSet<>();
        // Check if the teacher has exception availabilities for this date
        if (teacherRepository.hasExceptionAvailability(teacherId, date)) {
            // Fetch the exception availabilities for this date
            List<TeacherExceptionAvailability> exceptionAvailabilities = teacherRepository.getTeacherExceptionAvailabilities(teacherId, date);
            for (TeacherExceptionAvailability exceptionAvailability : exceptionAvailabilities) {
                timeslots.addAll(generateTimeslots(exceptionAvailability.getStartTime(), exceptionAvailability.getEndTime()));
            }
        } else {
            // Fetch the recurring availabilities for this date
            List<TeacherRecurringAvailability> recurringAvailabilities = teacherRepository.getTeacherRecurringAvailabilities(teacherId, date);
            for (TeacherRecurringAvailability recurringAvailability : recurringAvailabilities) {
                timeslots.addAll(generateTimeslots(recurringAvailability.getStartTime(), recurringAvailability.getEndTime()));
            }
        }

        // Check if the teacher has scheduled classes on this date
        List<ScheduledClass> scheduledClasses = teacherRepository.getScheduledClasses(teacherId, date);
        scheduledClasses.forEach(scheduledClass -> {
            // Remove the timeslots that are already booked
            timeslots.remove(scheduledClass.getStartTime());
        });

        availability.setTimeslots(timeslots);
        return availability;
    }

    // Generate timeslots between startTime and endTime with 1-hour intervals
    private Set<LocalTime> generateTimeslots(LocalTime startTime, LocalTime endTime) {
        Set<LocalTime> timeslots = new LinkedHashSet<>();
        while (startTime.isBefore(endTime)) {
            timeslots.add(startTime);
            startTime = startTime.plusHours(1);
        }
        return timeslots;
    }
}
