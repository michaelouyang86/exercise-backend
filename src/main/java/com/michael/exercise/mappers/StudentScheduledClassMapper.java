package com.michael.exercise.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.michael.exercise.dtos.ScheduledClassResponse;
import com.michael.exercise.dtos.ScheduledClassesResponse;
import com.michael.exercise.models.ScheduledClassWithName;

@Mapper(componentModel = "spring")
public interface StudentScheduledClassMapper {

    default ScheduledClassesResponse toScheduledClassesResponse(List<ScheduledClassWithName> scheduledClassesWithName) {
        List<ScheduledClassResponse> scheduledClasses = scheduledClassesWithName.stream()
            .map(this::toScheduledClassResponse)
            .toList();
        ScheduledClassesResponse response = new ScheduledClassesResponse();
        response.setScheduledClasses(scheduledClasses);
        return response;
    }

    ScheduledClassResponse toScheduledClassResponse(ScheduledClassWithName scheduledClassWithName);
}
