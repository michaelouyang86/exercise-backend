package com.michael.exercise.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.michael.exercise.dtos.ScheduleClassRequest;
import com.michael.exercise.dtos.ScheduledClassesResponse;
import com.michael.exercise.dtos.StudentPointsRecordsResponse;
import com.michael.exercise.dtos.StudentPointsResponse;
import com.michael.exercise.dtos.TeacherAvailabilitiesResponse;
import com.michael.exercise.dtos.TeachersForStudentResponse;
import com.michael.exercise.mappers.StudentPointMapper;
import com.michael.exercise.mappers.StudentScheduledClassMapper;
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
    private final StudentScheduledClassMapper studentScheduledClassMapper;
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
    public ResponseEntity<ScheduledClassesResponse> listUpcomingScheduledClasses() {
        int studentId = SecurityUtil.getUserId();
        List<ScheduledClassWithName> scheduledClassesWithName = studentService.listUpcomingScheduledClasses(studentId);
        ScheduledClassesResponse response = studentScheduledClassMapper.toScheduledClassesResponse(scheduledClassesWithName);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Override
    public ResponseEntity<ScheduledClassesResponse> listPastScheduledClasses() {
        int studentId = SecurityUtil.getUserId();
        List<ScheduledClassWithName> scheduledClassesWithName = studentService.listPastScheduledClasses(studentId);
        ScheduledClassesResponse response = studentScheduledClassMapper.toScheduledClassesResponse(scheduledClassesWithName);
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
