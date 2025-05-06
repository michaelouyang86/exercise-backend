package com.michael.exercise.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.michael.exercise.dtos.StudentPointsRecordResponse;
import com.michael.exercise.dtos.StudentPointsRecordsResponse;
import com.michael.exercise.dtos.StudentPointsResponse;
import com.michael.exercise.models.StudentPointsRecord;

@Mapper(componentModel = "spring")
public interface StudentPointMapper {

    default StudentPointsResponse toStudentPointsResponse(int points) {
        StudentPointsResponse response = new StudentPointsResponse();
        response.setPoints(points);
        return response;
    }

    default StudentPointsRecordsResponse toStudentPointsRecordsResponse(List<StudentPointsRecord> studentPointsRecords) {
        List<StudentPointsRecordResponse> records = studentPointsRecords.stream()
            .map(this::toStudentPointsRecordResponse)
            .toList();
        StudentPointsRecordsResponse response = new StudentPointsRecordsResponse();
        response.setRecords(records);
        return response;
    }

    StudentPointsRecordResponse toStudentPointsRecordResponse(StudentPointsRecord studentPointsRecord);
}
