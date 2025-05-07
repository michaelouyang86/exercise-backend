package com.michael.exercise.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.michael.exercise.dtos.CreateStudentRequest;
import com.michael.exercise.dtos.CreateTeacherRequest;
import com.michael.exercise.dtos.StudentResponse;
import com.michael.exercise.dtos.TeachersForAdminResponse;
import com.michael.exercise.mappers.StudentMapper;
import com.michael.exercise.mappers.TeacherMapper;
import com.michael.exercise.models.Student;
import com.michael.exercise.models.Teacher;
import com.michael.exercise.services.AdminService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AdminController implements AdminApi {

    private final AdminService adminService;
    private final StudentMapper studentMapper;
    private final TeacherMapper teacherMapper;

    @Override
    public ResponseEntity<Void> createStudent(CreateStudentRequest request) {
        adminService.createStudent(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    public ResponseEntity<StudentResponse> getStudentByPhone(String studentPhone) {
        Student student = adminService.getStudentByPhone(studentPhone);
        StudentResponse response = studentMapper.toStudentResponse(student);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Override
    public ResponseEntity<Void> deleteStudent(Integer studentId) {
        adminService.deleteStudent(studentId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @Override
    public ResponseEntity<Void> createTeacher(CreateTeacherRequest request) {
        adminService.createTeacher(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    public ResponseEntity<TeachersForAdminResponse> listTeachersForAdmin() {
        List<Teacher> teachers = adminService.listTeachersForAdmin();
        TeachersForAdminResponse response = teacherMapper.toTeachersForAdminResponse(teachers);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Override
    public ResponseEntity<Void> deleteTeacher(Integer teacherId) {
        adminService.deleteTeacher(teacherId);
        return ResponseEntity
                .noContent()
                .build();
    }
}
