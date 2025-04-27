package com.michael.exercise.services;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.michael.exercise.dtos.CreateStudentRequest;
import com.michael.exercise.dtos.ScheduleClassRequest;
import com.michael.exercise.models.ScheduledClass;
import com.michael.exercise.models.Student;
import com.michael.exercise.repositories.StudentRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public Student getStudentByPhone(String phone) {
        return studentRepository.getStudentByPhone(phone);
    }

    public void createStudent(CreateStudentRequest request) {
        String password = request.getPhone().substring(7);
        studentRepository.createStudent(request, passwordEncoder.encode(password));
    }

    public void deleteStudent(int id) {
        studentRepository.deleteStudent(id);
    }

    public void scheduleClass(int studentId, ScheduleClassRequest request) {
        studentRepository.scheduleClass(studentId, request);
    }

    public List<ScheduledClass> getScheduledClasses(int studentId) {
        return studentRepository.getScheduledClasses(studentId);
    }

    public void cancelScheduledClass(int studentId, int scheduledClassId) {
        studentRepository.cancelScheduledClass(studentId, scheduledClassId);
    }
}
