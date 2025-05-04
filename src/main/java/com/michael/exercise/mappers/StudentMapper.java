package com.michael.exercise.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.michael.exercise.dtos.ScheduledClassResponse;
import com.michael.exercise.dtos.StudentResponse;
import com.michael.exercise.models.ScheduledClassWithName;
import com.michael.exercise.models.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentResponse toStudentResponse(Student student);

    List<ScheduledClassResponse> toScheduledClassResponseList(List<ScheduledClassWithName> scheduledClassesWithName);
}
