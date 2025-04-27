package com.michael.exercise.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.michael.exercise.dtos.AddTeacherExceptionAvailabilityRequest;
import com.michael.exercise.dtos.AddTeacherRecurringAvailabilityRequest;
import com.michael.exercise.dtos.AddTeacherUnavailableDateRequest;
import com.michael.exercise.dtos.GetTeacherExceptionAvailabilityResponse;
import com.michael.exercise.dtos.GetTeacherRecurringAvailabilityResponse;
import com.michael.exercise.dtos.GetTeacherUnavailableDateResponse;
import com.michael.exercise.mappers.TeacherMapper;
import com.michael.exercise.models.AddTeacherExceptionAvailability;
import com.michael.exercise.models.AddTeacherRecurringAvailability;
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
    private final TeacherMapper teacherMapper;

    @Override
    public ResponseEntity<Void> addTeacherRecurringAvailability(AddTeacherRecurringAvailabilityRequest request) {
        int teacherId = SecurityUtil.getTeacherId();
        AddTeacherRecurringAvailability recurringAvailability = teacherMapper.toAddTeacherRecurringAvailability(teacherId, request);
        teacherService.addTeacherRecurringAvailability(recurringAvailability);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    public ResponseEntity<List<GetTeacherRecurringAvailabilityResponse>> getTeacherRecurringAvailabilities() {
        int teacherId = SecurityUtil.getTeacherId();
        List<TeacherRecurringAvailability> teacherRecurringAvailabilities = teacherService.getTeacherRecurringAvailabilities(teacherId);
        List<GetTeacherRecurringAvailabilityResponse> response = teacherMapper.toGetRecurringAvailabilityResponseList(teacherRecurringAvailabilities);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Override
    public ResponseEntity<Void> deleteTeacherRecurringAvailability(Integer recurringId) {
        int teacherId = SecurityUtil.getTeacherId();
        teacherService.deleteTeacherRecurringAvailability(teacherId, recurringId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @Override
    public ResponseEntity<Void> addTeacherExceptionAvailability(AddTeacherExceptionAvailabilityRequest request) {
        int teacherId = SecurityUtil.getTeacherId();
        AddTeacherExceptionAvailability exceptionAvailability = teacherMapper.toAddTeacherExceptionAvailability(teacherId, request);
        teacherService.addTeacherExceptionAvailability(exceptionAvailability);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    public ResponseEntity<List<GetTeacherExceptionAvailabilityResponse>> getTeacherExceptionAvailabilities() {
        int teacherId = SecurityUtil.getTeacherId();
        List<TeacherExceptionAvailability> teacherExceptionAvailabilities = teacherService.getTeacherExceptionAvailabilities(teacherId);
        List<GetTeacherExceptionAvailabilityResponse> response = teacherMapper.toGetTeacherExceptionAvailabilityResponseList(teacherExceptionAvailabilities);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Override
    public ResponseEntity<Void> deleteTeacherExceptionAvailability(Integer exceptionId) {
        int teacherId = SecurityUtil.getTeacherId();
        teacherService.deleteTeacherExceptionAvailability(teacherId, exceptionId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @Override
    public ResponseEntity<Void> addTeacherUnavailableDate(AddTeacherUnavailableDateRequest request) {
        int teacherId = SecurityUtil.getTeacherId();
        teacherService.addTeacherUnavailableDate(teacherId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    public ResponseEntity<List<GetTeacherUnavailableDateResponse>> getTeacherUnavailableDates() {
        int teacherId = SecurityUtil.getTeacherId();
        List<TeacherUnavailableDate> teacherUnavailableDates = teacherService.getTeacherUnavailableDates(teacherId);
        List<GetTeacherUnavailableDateResponse> response = teacherMapper.toGetTeacherUnavailableDateResponseList(teacherUnavailableDates);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Override
    public ResponseEntity<Void> deleteTeacherUnavailableDate(Integer unavailableDateId) {
        int teacherId = SecurityUtil.getTeacherId();
        teacherService.deleteTeacherUnavailableDate(teacherId, unavailableDateId);
        return ResponseEntity
                .noContent()
                .build();
    }
}
