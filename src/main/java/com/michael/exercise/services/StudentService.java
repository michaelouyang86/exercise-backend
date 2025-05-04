package com.michael.exercise.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.michael.exercise.dtos.ScheduleClassRequest;
import com.michael.exercise.exception.BadRequestException;
import com.michael.exercise.models.ScheduledClass;
import com.michael.exercise.models.ScheduledClassWithName;
import com.michael.exercise.models.TeacherAvailability;
import com.michael.exercise.models.TeacherExceptionAvailability;
import com.michael.exercise.models.TeacherIdAndName;
import com.michael.exercise.models.TeacherRecurringAvailability;
import com.michael.exercise.repositories.ScheduledClassRepository;
import com.michael.exercise.repositories.TeacherExceptionAvailabilityRepository;
import com.michael.exercise.repositories.TeacherRecurringAvailabilityRepository;
import com.michael.exercise.repositories.TeacherUnavailableDateRepository;
import com.michael.exercise.repositories.UserRepository;
import com.michael.exercise.services.common.StudentPointService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StudentService {

    private final StudentPointService studentPointService;
    private final UserRepository userRepository; 
    private final TeacherUnavailableDateRepository teacherUnavailableDateRepository;
    private final TeacherExceptionAvailabilityRepository teacherExceptionAvailabilityRepository;
    private final TeacherRecurringAvailabilityRepository teacherRecurringAvailabilityRepository;
    private final ScheduledClassRepository scheduledClassRepository;

    public List<TeacherIdAndName> listTeachersForStudent() {
        return userRepository.listTeachersIdAndName();
    }

    // Get the availabilities for a specific teacher for a specific week
    public List<TeacherAvailability> fetchTeacherAvailabilities(int teacherId, String isoWeek) {
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
        teacherAvailabilities.add(fetchTeacherAvailability(teacherId, monday));
        teacherAvailabilities.add(fetchTeacherAvailability(teacherId, tuesday));
        teacherAvailabilities.add(fetchTeacherAvailability(teacherId, wednesday));
        teacherAvailabilities.add(fetchTeacherAvailability(teacherId, thursday));
        teacherAvailabilities.add(fetchTeacherAvailability(teacherId, friday));
        teacherAvailabilities.add(fetchTeacherAvailability(teacherId, saturday));
        teacherAvailabilities.add(fetchTeacherAvailability(teacherId, sunday));
        return teacherAvailabilities;
    }

    // Get the availability for a specific teacher on a specific date
    private TeacherAvailability fetchTeacherAvailability(int teacherId, LocalDate date) {
        TeacherAvailability availability = new TeacherAvailability();
        availability.setDate(date);

        String dayOfWeek = date.getDayOfWeek().name();
        availability.setDayOfWeek(dayOfWeek);

        // Check if the teacher is unavailable on this date
        boolean isUnavailable = teacherUnavailableDateRepository.isUnavailable(teacherId, date);
        if (isUnavailable) {
            availability.setTimeslots(Collections.emptySet());
            return availability;
        }

        Set<LocalTime> timeslots = new LinkedHashSet<>();
        // Check if the teacher has exception availabilities for this date
        if (teacherExceptionAvailabilityRepository.hasExceptionAvailability(teacherId, date)) {
            // Fetch the exception availabilities for this date
            List<TeacherExceptionAvailability> exceptionAvailabilities = teacherExceptionAvailabilityRepository.listTeacherExceptionAvailabilities(teacherId, date);
            for (TeacherExceptionAvailability exceptionAvailability : exceptionAvailabilities) {
                timeslots.addAll(generateTimeslots(exceptionAvailability.getStartTime(), exceptionAvailability.getEndTime()));
            }
        } else {
            // Fetch the recurring availabilities for this date
            List<TeacherRecurringAvailability> recurringAvailabilities = teacherRecurringAvailabilityRepository.listTeacherRecurringAvailabilities(teacherId, date);
            for (TeacherRecurringAvailability recurringAvailability : recurringAvailabilities) {
                timeslots.addAll(generateTimeslots(recurringAvailability.getStartTime(), recurringAvailability.getEndTime()));
            }
        }

        // Check if the teacher has scheduled classes on this date
        List<ScheduledClass> scheduledClasses = scheduledClassRepository.listTeacherScheduledClasses(teacherId, date);
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

    @Transactional
    public void scheduleClass(int studentId, ScheduleClassRequest request) {
        // Check if the student has enough points to schedule the class
        int currentPoints = studentPointService.getStudentPointsForUpdate(studentId);
        if (currentPoints <= 0) {
            throw new BadRequestException("insufficient_points", "學生點數不足，無法預約課程");
        }
        // Schedule the class
        scheduledClassRepository.scheduleClass(studentId, request);
        // Deduct points from the student
        studentPointService.deductOnePointFromStudent(studentId, currentPoints);
    }

    public List<ScheduledClassWithName> listScheduledClasses(int studentId, boolean isPast) {
        if (isPast) {
            return scheduledClassRepository.listStudentPastScheduledClasses(studentId);
        }
        return scheduledClassRepository.listStudentScheduledClasses(studentId);
    }

    @Transactional
    public void cancelClass(int studentId, int scheduledClassId) {
        // Cancel the scheduled class
        scheduledClassRepository.cancelClass(studentId, scheduledClassId);
        // Refund points to the student
        int currentPoints = studentPointService.getStudentPointsForUpdate(studentId);
        studentPointService.refundOnePointToStudent(studentId, currentPoints);
    }
}
