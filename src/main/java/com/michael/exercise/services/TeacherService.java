package com.michael.exercise.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.michael.exercise.dtos.AddTeacherUnavailableDateRequest;
import com.michael.exercise.models.TeacherRecurringAvailability;
import com.michael.exercise.models.TeacherUnavailableDate;
import com.michael.exercise.models.AddTeacherExceptionAvailability;
import com.michael.exercise.models.AddTeacherRecurringAvailability;
import com.michael.exercise.models.TeacherExceptionAvailability;
import com.michael.exercise.repositories.TeacherExceptionAvailabilityRepository;
import com.michael.exercise.repositories.TeacherRecurringAvailabilityRepository;
import com.michael.exercise.repositories.TeacherUnavailableDateRepository;

import lombok.AllArgsConstructor;
import java.time.LocalTime;

@Service
@AllArgsConstructor
public class TeacherService {

    private final TeacherRecurringAvailabilityRepository teacherRecurringAvailabilityRepository;
    private final TeacherExceptionAvailabilityRepository teacherExceptionAvailabilityRepository;
    private final TeacherUnavailableDateRepository teacherUnavailableDateRepository;

    public void addTeacherRecurringAvailability(AddTeacherRecurringAvailability availability) {
        LocalTime startTime = availability.getStartTime();
        LocalTime endTime = availability.getEndTime();
        if (startTime.equals(endTime) || startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }
        teacherRecurringAvailabilityRepository.addTeacherRecurringAvailability(availability);
    }

    public List<TeacherRecurringAvailability> listTeacherRecurringAvailabilities(int teacherId) {
        return teacherRecurringAvailabilityRepository.listTeacherRecurringAvailabilities(teacherId);
    }

    public void deleteTeacherRecurringAvailability(int teacherId, int recurringId) {
        teacherRecurringAvailabilityRepository.deleteTeacherRecurringAvailability(teacherId, recurringId);
    }

    public void addTeacherExceptionAvailability(AddTeacherExceptionAvailability exceptionAvailability) {
        teacherExceptionAvailabilityRepository.addTeacherExceptionAvailability(exceptionAvailability);
    }

    public List<TeacherExceptionAvailability> listUpcomingTeacherExceptionAvailabilities(int teacherId) {
        return teacherExceptionAvailabilityRepository.listUpcomingTeacherExceptionAvailabilities(teacherId);
    }

    public void deleteTeacherExceptionAvailability(int teacherId, int exceptionId) {
        teacherExceptionAvailabilityRepository.deleteTeacherExceptionAvailability(teacherId, exceptionId);
    }

    public void addTeacherUnavailableDate(int teacherId, AddTeacherUnavailableDateRequest request) {
        teacherUnavailableDateRepository.addTeacherUnavailableDate(teacherId, request);
    }

    public List<TeacherUnavailableDate> listUpcomingTeacherUnavailableDates(int teacherId) {
        return teacherUnavailableDateRepository.listUpcomingTeacherUnavailableDates(teacherId);
    }

    public void deleteTeacherUnavailableDate(int teacherId, int unavailableDateId) {
        teacherUnavailableDateRepository.deleteTeacherUnavailableDate(teacherId, unavailableDateId);
    }
}
