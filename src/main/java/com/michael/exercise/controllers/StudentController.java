package com.michael.exercise.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.michael.exercise.dtos.ScheduleClassRequest;
import com.michael.exercise.dtos.StudentPointsRecordsResponse;
import com.michael.exercise.dtos.StudentPointsResponse;
import com.michael.exercise.dtos.StudentScheduledClassesResponse;
import com.michael.exercise.dtos.TeacherAvailabilitiesResponse;
import com.michael.exercise.dtos.TeachersForStudentResponse;
import com.michael.exercise.exception.BadRequestException;
import com.michael.exercise.mappers.StudentPointMapper;
import com.michael.exercise.mappers.ScheduledClassMapper;
import com.michael.exercise.mappers.TeacherAvailabilityMapper;
import com.michael.exercise.mappers.TeacherMapper;
import com.michael.exercise.models.ScheduledClassWithName;
import com.michael.exercise.models.StudentPointsRecord;
import com.michael.exercise.models.TeacherAvailability;
import com.michael.exercise.models.TeacherIdAndName;
import com.michael.exercise.security.SecurityUtil;
import com.michael.exercise.services.StudentService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class StudentController implements StudentApi {

    private final StudentService studentService;
    private final TeacherMapper teacherMapper;
    private final TeacherAvailabilityMapper teacherAvailabilityMapper;
    private final ScheduledClassMapper scheduledClassMapper;
    private final StudentPointMapper studentPointMapper;

    @Override
    public ResponseEntity<TeachersForStudentResponse> listTeachersForStudent() {
        List<TeacherIdAndName> teachersIdAndName = studentService.listTeachersForStudent();
        TeachersForStudentResponse response = teacherMapper.toTeachersForStudentResponse(teachersIdAndName);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Override
    public ResponseEntity<TeacherAvailabilitiesResponse> fetchTeacherAvailabilities(Integer teacherId, String isoWeek) {
        List<TeacherAvailability> teacherAvailabilities = studentService.fetchTeacherAvailabilities(teacherId, isoWeek);
        TeacherAvailabilitiesResponse response = teacherAvailabilityMapper.toTeacherAvailabilitiesResponse(teacherAvailabilities);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Override
    public ResponseEntity<Void> scheduleClass(ScheduleClassRequest request) {
        int studentId = SecurityUtil.getUserId();
        studentService.scheduleClass(studentId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    public ResponseEntity<StudentScheduledClassesResponse> listStudentScheduledClasses(LocalDate startDate, LocalDate endDate) {
        int studentId = SecurityUtil.getUserId();
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
        List<ScheduledClassWithName> scheduledClassesWithName = studentService.listScheduledClasses(studentId, startDate, endDate);
        StudentScheduledClassesResponse response = scheduledClassMapper.toStudentScheduledClassesResponse(scheduledClassesWithName);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Override
    public ResponseEntity<Void> cancelClassByStudent(Integer scheduleId) {
        int studentId = SecurityUtil.getUserId();
        ScheduledClassWithName scheduledClassWithName = studentService.getScheduledClass(scheduleId);
        studentService.cancelClass(studentId, scheduledClassWithName);
        return ResponseEntity
                .noContent()
                .build();
    }

    @Override
    public ResponseEntity<StudentPointsResponse> getStudentPoints() {
        int studentId = SecurityUtil.getUserId();
        int points = studentService.getStudentPoints(studentId);
        StudentPointsResponse response = studentPointMapper.toStudentPointsResponse(points);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Override
    public ResponseEntity<StudentPointsRecordsResponse> listStudentPointsRecords() {
        int studentId = SecurityUtil.getUserId();
        List<StudentPointsRecord> studentPointsRecords = studentService.listStudentPointsRecords(studentId);
        StudentPointsRecordsResponse response = studentPointMapper.toStudentPointsRecordsResponse(studentPointsRecords);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
