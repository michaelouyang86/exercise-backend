package com.michael.exercise.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.michael.exercise.dtos.AddTeacherExceptionAvailabilityRequest;
import com.michael.exercise.dtos.AddTeacherRecurringAvailabilityRequest;
import com.michael.exercise.dtos.AddTeacherUnavailableDateRequest;
import com.michael.exercise.dtos.TeacherExceptionAvailabilitiesResponse;
import com.michael.exercise.dtos.TeacherRecurringAvailabilitiesResponse;
import com.michael.exercise.dtos.TeacherScheduledClassesResponse;
import com.michael.exercise.dtos.TeacherUnavailableDatesResponse;
import com.michael.exercise.exception.BadRequestException;
import com.michael.exercise.mappers.ScheduledClassMapper;
import com.michael.exercise.mappers.TeacherAvailabilityMapper;
import com.michael.exercise.models.AddTeacherExceptionAvailability;
import com.michael.exercise.models.AddTeacherRecurringAvailability;
import com.michael.exercise.models.ScheduledClassWithName;
import com.michael.exercise.models.TeacherExceptionAvailability;
import com.michael.exercise.models.TeacherRecurringAvailability;
import com.michael.exercise.models.TeacherUnavailableDate;
import com.michael.exercise.security.SecurityUtil;
import com.michael.exercise.services.TeacherService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class TeacherController implements TeacherApi {

    private final TeacherService teacherService;
    private final ScheduledClassMapper scheduledClassMapper;
    private final TeacherAvailabilityMapper teacherAvailabilityMapper;

    @Override
    public ResponseEntity<TeacherScheduledClassesResponse> listTeacherScheduledClasses(LocalDate startDate, LocalDate endDate) {
        int teacherId = SecurityUtil.getUserId();
        // Start date defaults to today
        if (startDate == null) {
            startDate = LocalDate.now();
        }
        // End date defaults to forever
        if (endDate == null) {
            endDate = LocalDate.of(9999, 12, 31);
        }
        // Validate date range
        if (startDate.isAfter(endDate)) {
            throw new BadRequestException("invalid_date_range", "日期範圍錯誤");
        }
        List<ScheduledClassWithName> scheduledClassesWithName = teacherService.listScheduledClasses(teacherId, startDate, endDate);
        TeacherScheduledClassesResponse response = scheduledClassMapper.toTeacherScheduledClassesResponse(scheduledClassesWithName);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Override
    public ResponseEntity<Void> cancelClassByTeacher(Integer scheduleId) {
        int teacherId = SecurityUtil.getUserId();
        ScheduledClassWithName scheduledClassWithName = teacherService.getScheduledClass(scheduleId);
        teacherService.cancelClass(teacherId, scheduledClassWithName);
        return ResponseEntity
                .noContent()
                .build();
    }

    @Override
    public ResponseEntity<Void> addTeacherRecurringAvailability(AddTeacherRecurringAvailabilityRequest request) {
        int teacherId = SecurityUtil.getUserId();
        AddTeacherRecurringAvailability recurringAvailability = teacherAvailabilityMapper.toAddTeacherRecurringAvailability(teacherId, request);
        teacherService.addTeacherRecurringAvailability(recurringAvailability);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    public ResponseEntity<TeacherRecurringAvailabilitiesResponse> listTeacherRecurringAvailabilities() {
        int teacherId = SecurityUtil.getUserId();
        List<TeacherRecurringAvailability> teacherRecurringAvailabilities = teacherService.listTeacherRecurringAvailabilities(teacherId);
        TeacherRecurringAvailabilitiesResponse response = teacherAvailabilityMapper.toTeacherRecurringAvailabilitiesResponse(teacherRecurringAvailabilities);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Override
    public ResponseEntity<Void> deleteTeacherRecurringAvailability(Integer recurringId) {
        int teacherId = SecurityUtil.getUserId();
        teacherService.deleteTeacherRecurringAvailability(teacherId, recurringId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @Override
    public ResponseEntity<Void> addTeacherExceptionAvailability(AddTeacherExceptionAvailabilityRequest request) {
        int teacherId = SecurityUtil.getUserId();
        AddTeacherExceptionAvailability exceptionAvailability = teacherAvailabilityMapper.toAddTeacherExceptionAvailability(teacherId, request);
        teacherService.addTeacherExceptionAvailability(exceptionAvailability);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    public ResponseEntity<TeacherExceptionAvailabilitiesResponse> listUpcomingTeacherExceptionAvailabilities() {
        int teacherId = SecurityUtil.getUserId();
        List<TeacherExceptionAvailability> teacherExceptionAvailabilities = teacherService.listUpcomingTeacherExceptionAvailabilities(teacherId);
        TeacherExceptionAvailabilitiesResponse response = teacherAvailabilityMapper.toTeacherExceptionAvailabilitiesResponse(teacherExceptionAvailabilities);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Override
    public ResponseEntity<Void> deleteTeacherExceptionAvailability(Integer exceptionId) {
        int teacherId = SecurityUtil.getUserId();
        teacherService.deleteTeacherExceptionAvailability(teacherId, exceptionId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @Override
    public ResponseEntity<Void> addTeacherUnavailableDate(AddTeacherUnavailableDateRequest request) {
        int teacherId = SecurityUtil.getUserId();
        teacherService.addTeacherUnavailableDate(teacherId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    public ResponseEntity<TeacherUnavailableDatesResponse> listUpcomingTeacherUnavailableDates() {
        int teacherId = SecurityUtil.getUserId();
        List<TeacherUnavailableDate> teacherUnavailableDates = teacherService.listUpcomingTeacherUnavailableDates(teacherId);
        TeacherUnavailableDatesResponse response = teacherAvailabilityMapper.toTeacherUnavailableDatesResponse(teacherUnavailableDates);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Override
    public ResponseEntity<Void> deleteTeacherUnavailableDate(Integer unavailableDateId) {
        int teacherId = SecurityUtil.getUserId();
        teacherService.deleteTeacherUnavailableDate(teacherId, unavailableDateId);
        return ResponseEntity
                .noContent()
                .build();
    }
}
