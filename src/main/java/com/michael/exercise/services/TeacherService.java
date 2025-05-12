package com.michael.exercise.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.michael.exercise.dtos.AddTeacherUnavailableDateRequest;
import com.michael.exercise.exception.BadRequestException;
import com.michael.exercise.models.TeacherRecurringAvailability;
import com.michael.exercise.models.TeacherUnavailableDate;
import com.michael.exercise.models.AddTeacherExceptionAvailability;
import com.michael.exercise.models.AddTeacherRecurringAvailability;
import com.michael.exercise.models.ScheduledClassWithName;
import com.michael.exercise.models.TeacherExceptionAvailability;
import com.michael.exercise.repositories.ScheduledClassRepository;
import com.michael.exercise.repositories.TeacherExceptionAvailabilityRepository;
import com.michael.exercise.repositories.TeacherRecurringAvailabilityRepository;
import com.michael.exercise.repositories.TeacherUnavailableDateRepository;
import com.michael.exercise.services.common.StudentPointService;

import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@AllArgsConstructor
public class TeacherService {

    private final StudentPointService studentPointService;
    private final ScheduledClassRepository scheduledClassRepository;
    private final TeacherRecurringAvailabilityRepository teacherRecurringAvailabilityRepository;
    private final TeacherExceptionAvailabilityRepository teacherExceptionAvailabilityRepository;
    private final TeacherUnavailableDateRepository teacherUnavailableDateRepository;

    public List<ScheduledClassWithName> listScheduledClasses(int teacherId, LocalDate startDate, LocalDate endDate) {
        return scheduledClassRepository.listTeacherScheduledClasses(teacherId, startDate, endDate);
    }

    public ScheduledClassWithName getScheduledClass(int id) {
        return scheduledClassRepository.getScheduledClass(id);
    }

    @Transactional
    public void cancelClass(int teacherId, ScheduledClassWithName scheduledClassWithName) {
        // Cancel the scheduled class
        int affectedRows = scheduledClassRepository.cancelTeacherClass(teacherId, scheduledClassWithName.getId());
        if (affectedRows != 1) {
            throw new BadRequestException("cancel_class_failed", "取消預約失敗");
        }
        // Refund points to the student
        int studentId = scheduledClassWithName.getStudentId();
        int currentPoints = studentPointService.getStudentPointsForUpdate(studentId);
        String reason = String.format("[老師取消] %s, %s, %s", scheduledClassWithName.getTeacherName(), scheduledClassWithName.getClassDate(), scheduledClassWithName.getStartTime());
        studentPointService.refundOnePointToStudent(studentId, currentPoints, reason);
    }

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
