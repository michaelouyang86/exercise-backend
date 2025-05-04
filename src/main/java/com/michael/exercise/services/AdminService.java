package com.michael.exercise.services;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.michael.exercise.dtos.CreateStudentRequest;
import com.michael.exercise.dtos.CreateTeacherRequest;
import com.michael.exercise.models.Student;
import com.michael.exercise.models.Teacher;
import com.michael.exercise.repositories.UserRepository;
import com.michael.exercise.services.common.StudentPointService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdminService {

    private final PasswordEncoder passwordEncoder;
    private final StudentPointService studentPointService;
    private final UserRepository userRepository;

    @Transactional
    public void createStudent(CreateStudentRequest request) {
        // Password default to phone number last 3 digits
        String password = request.getPhone().substring(7);
        String encodedPassword = passwordEncoder.encode(password);
        int studentId = userRepository.createStudent(request, encodedPassword);
        studentPointService.initializeStudentPoints(studentId, request.getPoints());
    }

    public Student getStudentByPhone(String phone) {
        return userRepository.getStudentByPhone(phone);
    }

    public void deleteStudent(int id) {
        userRepository.deleteStudent(id);
    }

    public void createTeacher(CreateTeacherRequest request) {
        // Password default to phone number last 3 digits
        String password = request.getPhone().substring(7);
        String encodedPassword = passwordEncoder.encode(password);
        userRepository.createTeacher(request, encodedPassword);
    }

    public List<Teacher> listTeachersForAdmin() {
        return userRepository.listTeachers();
    }

    public void deleteTeacher(int id) {
        userRepository.deleteTeacher(id);
    }
}
