package com.michael.exercise.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.michael.exercise.dtos.GetScheduledClassResponse;
import com.michael.exercise.dtos.GetTeacherAvailabilityResponse;
import com.michael.exercise.dtos.ScheduleClassRequest;
import com.michael.exercise.dtos.StudentGetTeacherResponse;
import com.michael.exercise.mappers.StudentMapper;
import com.michael.exercise.mappers.TeacherMapper;
import com.michael.exercise.models.ScheduledClass;
import com.michael.exercise.models.Teacher;
import com.michael.exercise.models.TeacherAvailability;
import com.michael.exercise.security.SecurityUtil;
import com.michael.exercise.services.StudentService;
import com.michael.exercise.services.TeacherService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class StudentController implements StudentApi {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final StudentMapper studentMapper;
    private final TeacherMapper teacherMapper;

    @Override
    public ResponseEntity<List<StudentGetTeacherResponse>> getTeachersForStudent() {
        List<Teacher> teachers = teacherService.getTeachers();
        List<StudentGetTeacherResponse> response = teacherMapper.toStudentGetTeacherResponseList(teachers);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Override
    public ResponseEntity<List<GetTeacherAvailabilityResponse>> getTeacherAvailabilities(Integer teacherId, String isoWeek) {
        List<TeacherAvailability> teacherAvailabilities = teacherService.getTeacherAvailabilities(teacherId, isoWeek);
        List<GetTeacherAvailabilityResponse> response = teacherMapper.toGetTeacherAvailabilityResponseList(teacherAvailabilities);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Override
    public ResponseEntity<Void> scheduleClass(ScheduleClassRequest request) {
        int studentId = SecurityUtil.getStudentId();
        studentService.scheduleClass(studentId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    public ResponseEntity<List<GetScheduledClassResponse>> getScheduledClasses() {
        int studentId = SecurityUtil.getStudentId();
        List<ScheduledClass> scheduleClasses = studentService.getScheduledClasses(studentId);
        List<GetScheduledClassResponse> response = studentMapper.toGetScheduledClassResponseList(scheduleClasses);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Override
    public ResponseEntity<Void> cancelScheduledClass(Integer scheduleId) {
        int studentId = SecurityUtil.getStudentId();
        studentService.cancelScheduledClass(studentId, scheduleId);
        return ResponseEntity
                .noContent()
                .build();
    }
}
