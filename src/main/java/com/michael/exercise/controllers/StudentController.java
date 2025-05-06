package com.michael.exercise.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.michael.exercise.dtos.ScheduleClassRequest;
import com.michael.exercise.dtos.ScheduledClassResponse;
import com.michael.exercise.dtos.StudentPointsRecordsResponse;
import com.michael.exercise.dtos.StudentPointsResponse;
import com.michael.exercise.dtos.TeacherAvailabilityResponse;
import com.michael.exercise.dtos.TeacherForStudentResponse;
import com.michael.exercise.mappers.StudentMapper;
import com.michael.exercise.mappers.StudentPointMapper;
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
    private final StudentMapper studentMapper;
    private final StudentPointMapper studentPointMapper;

    @Override
    public ResponseEntity<List<TeacherForStudentResponse>> listTeachersForStudent() {
        List<TeacherIdAndName> teachersIdAndName = studentService.listTeachersForStudent();
        List<TeacherForStudentResponse> response = teacherMapper.toTeacherForStudentResponseList(teachersIdAndName);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Override
    public ResponseEntity<List<TeacherAvailabilityResponse>> fetchTeacherAvailabilities(Integer teacherId, String isoWeek) {
        List<TeacherAvailability> teacherAvailabilities = studentService.fetchTeacherAvailabilities(teacherId, isoWeek);
        List<TeacherAvailabilityResponse> response = teacherMapper.toTeacherAvailabilityResponseList(teacherAvailabilities);
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
    public ResponseEntity<List<ScheduledClassResponse>> listScheduledClasses(Boolean isPast) {
        int studentId = SecurityUtil.getUserId();
        List<ScheduledClassWithName> scheduledClassesWithName = studentService.listScheduledClasses(studentId, isPast);
        List<ScheduledClassResponse> response = studentMapper.toScheduledClassResponseList(scheduledClassesWithName);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Override
    public ResponseEntity<Void> cancelClass(Integer scheduleId) {
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
