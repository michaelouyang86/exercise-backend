package com.michael.exercise.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.michael.exercise.dtos.AdminGetTeacherResponse;
import com.michael.exercise.dtos.CreateStudentRequest;
import com.michael.exercise.dtos.CreateTeacherRequest;
import com.michael.exercise.dtos.GetStudentResponse;
import com.michael.exercise.mappers.StudentMapper;
import com.michael.exercise.mappers.TeacherMapper;
import com.michael.exercise.models.Student;
import com.michael.exercise.models.Teacher;
import com.michael.exercise.services.StudentService;
import com.michael.exercise.services.TeacherService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AdminController implements AdminApi {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final StudentMapper studentMapper;
    private final TeacherMapper teacherMapper;

    @Override
    public ResponseEntity<Void> createStudent(CreateStudentRequest request) {
        studentService.createStudent(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    public ResponseEntity<GetStudentResponse> getStudentByPhone(String studentPhone) {
        Student student = studentService.getStudentByPhone(studentPhone);
        GetStudentResponse response = studentMapper.toGetStudentResponse(student);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Override
    public ResponseEntity<Void> deleteStudent(Integer studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @Override
    public ResponseEntity<Void> createTeacher(CreateTeacherRequest request) {
        teacherService.createTeacher(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    public ResponseEntity<List<AdminGetTeacherResponse>> getTeachersForAdmin() {
        List<Teacher> teachers = teacherService.getTeachers();
        List<AdminGetTeacherResponse> response = teacherMapper.toAdminGetTeacherResponseList(teachers);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Override
    public ResponseEntity<Void> deleteTeacher(Integer teacherId) {
        teacherService.deleteTeacher(teacherId);
        return ResponseEntity
                .noContent()
                .build();
    }
}
