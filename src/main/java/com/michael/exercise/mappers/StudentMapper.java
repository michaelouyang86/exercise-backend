package com.michael.exercise.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.michael.exercise.dtos.GetScheduledClassResponse;
import com.michael.exercise.dtos.GetStudentResponse;
import com.michael.exercise.models.ScheduledClass;
import com.michael.exercise.models.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    GetStudentResponse toGetStudentResponse(Student student);

    List<GetScheduledClassResponse> toGetScheduledClassResponseList(List<ScheduledClass> scheduledClasses);
}
