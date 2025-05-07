package com.michael.exercise.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.michael.exercise.dtos.TeacherForAdminResponse;
import com.michael.exercise.dtos.TeacherForStudentResponse;
import com.michael.exercise.dtos.TeachersForAdminResponse;
import com.michael.exercise.dtos.TeachersForStudentResponse;
import com.michael.exercise.models.Teacher;
import com.michael.exercise.models.TeacherIdAndName;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    default TeachersForStudentResponse toTeachersForStudentResponse(List<TeacherIdAndName> teachersIdAndName) {
        List<TeacherForStudentResponse> teacherResponses = teachersIdAndName.stream()
            .map(this::toTeacherIdAndName)
            .toList();
        TeachersForStudentResponse response = new TeachersForStudentResponse();
        response.setTeachers(teacherResponses);
        return response;
    }

    TeacherForStudentResponse toTeacherIdAndName(TeacherIdAndName teacherIdAndName);

    default TeachersForAdminResponse toTeachersForAdminResponse(List<Teacher> teachers) {
        List<TeacherForAdminResponse> teacherResponses = teachers.stream()
            .map(this::toTeacherForAdminResponse)
            .toList();
        TeachersForAdminResponse response = new TeachersForAdminResponse();
        response.setTeachers(teacherResponses);
        return response;
    }

    TeacherForAdminResponse toTeacherForAdminResponse(Teacher teacher);
}
