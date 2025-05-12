package com.michael.exercise.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.michael.exercise.dtos.StudentScheduledClassResponse;
import com.michael.exercise.dtos.StudentScheduledClassesResponse;
import com.michael.exercise.dtos.TeacherScheduledClassResponse;
import com.michael.exercise.dtos.TeacherScheduledClassesResponse;
import com.michael.exercise.models.ScheduledClassWithName;

@Mapper(componentModel = "spring")
public interface ScheduledClassMapper {

    // For Student
    default StudentScheduledClassesResponse toStudentScheduledClassesResponse(List<ScheduledClassWithName> scheduledClassesWithName) {
        List<StudentScheduledClassResponse> scheduledClasses = scheduledClassesWithName.stream()
            .map(this::toStudentScheduledClassResponse)
            .toList();
        StudentScheduledClassesResponse response = new StudentScheduledClassesResponse();
        response.setScheduledClasses(scheduledClasses);
        return response;
    }

    StudentScheduledClassResponse toStudentScheduledClassResponse(ScheduledClassWithName scheduledClassWithName);

    // For Teacher
    default TeacherScheduledClassesResponse toTeacherScheduledClassesResponse(List<ScheduledClassWithName> scheduledClassesWithName) {
        List<TeacherScheduledClassResponse> scheduledClasses = scheduledClassesWithName.stream()
            .map(this::toTeacherScheduledClassResponse)
            .toList();
        TeacherScheduledClassesResponse response = new TeacherScheduledClassesResponse();
        response.setScheduledClasses(scheduledClasses);
        return response;
    }

    TeacherScheduledClassResponse toTeacherScheduledClassResponse(ScheduledClassWithName scheduledClassWithName);
}
