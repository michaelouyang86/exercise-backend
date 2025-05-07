package com.michael.exercise.mappers;

import org.mapstruct.Mapper;

import com.michael.exercise.dtos.StudentResponse;
import com.michael.exercise.models.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentResponse toStudentResponse(Student student);
}
