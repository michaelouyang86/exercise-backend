package com.michael.exercise.mappers;

import java.time.LocalTime;
import java.util.List;

import org.mapstruct.Mapper;

import com.michael.exercise.dtos.AddTeacherExceptionAvailabilityRequest;
import com.michael.exercise.dtos.AddTeacherRecurringAvailabilityRequest;
import com.michael.exercise.dtos.TeacherAvailabilitiesResponse;
import com.michael.exercise.dtos.TeacherAvailabilityResponse;
import com.michael.exercise.dtos.TeacherExceptionAvailabilitiesResponse;
import com.michael.exercise.dtos.TeacherExceptionAvailabilityResponse;
import com.michael.exercise.dtos.TeacherRecurringAvailabilitiesResponse;
import com.michael.exercise.dtos.TeacherRecurringAvailabilityResponse;
import com.michael.exercise.dtos.TeacherUnavailableDateResponse;
import com.michael.exercise.dtos.TeacherUnavailableDatesResponse;
import com.michael.exercise.models.AddTeacherExceptionAvailability;
import com.michael.exercise.models.AddTeacherRecurringAvailability;
import com.michael.exercise.models.DayOfWeekEnum;
import com.michael.exercise.models.TeacherAvailability;
import com.michael.exercise.models.TeacherExceptionAvailability;
import com.michael.exercise.models.TeacherRecurringAvailability;
import com.michael.exercise.models.TeacherUnavailableDate;

@Mapper(componentModel = "spring")
public interface TeacherAvailabilityMapper {

    // Teacher availability
    default TeacherAvailabilitiesResponse toTeacherAvailabilitiesResponse(List<TeacherAvailability> teacherAvailabilities) {
        List<TeacherAvailabilityResponse> availabilities = teacherAvailabilities.stream()
            .map(this::toTeacherAvailabilityResponse)
            .toList();
        TeacherAvailabilitiesResponse response = new TeacherAvailabilitiesResponse();
        response.setTeacherAvailabilities(availabilities);
        return response;
    }

    TeacherAvailabilityResponse toTeacherAvailabilityResponse(TeacherAvailability teacherAvailability);

    // Recurring availability
    default AddTeacherRecurringAvailability toAddTeacherRecurringAvailability(int teacherId, AddTeacherRecurringAvailabilityRequest request) {
        AddTeacherRecurringAvailability recurringAvailability = new AddTeacherRecurringAvailability();
        recurringAvailability.setTeacherId(teacherId);
        recurringAvailability.setDayOfWeek(DayOfWeekEnum.fromValue(request.getDayOfWeek().getValue()));
        recurringAvailability.setStartTime(LocalTime.parse(request.getStartTime()));
        if ("24:00".equals(request.getEndTime())) {
            recurringAvailability.setEndTime(LocalTime.parse("23:59:59"));
        } else {
            recurringAvailability.setEndTime(LocalTime.parse(request.getEndTime()));
        }
        return recurringAvailability;
    }

    default TeacherRecurringAvailabilitiesResponse toTeacherRecurringAvailabilitiesResponse(List<TeacherRecurringAvailability> teacherRecurringAvailabilities) {
        List<TeacherRecurringAvailabilityResponse> recurringAvailabilities = teacherRecurringAvailabilities.stream()
            .map(this::toTeacherRecurringAvailabilityResponse)
            .toList();
        TeacherRecurringAvailabilitiesResponse response = new TeacherRecurringAvailabilitiesResponse();
        response.setRecurringAvailabilities(recurringAvailabilities);
        return response;
    }

    default TeacherRecurringAvailabilityResponse toTeacherRecurringAvailabilityResponse(TeacherRecurringAvailability teacherRecurringAvailability) {
        TeacherRecurringAvailabilityResponse response = new TeacherRecurringAvailabilityResponse();
        response.setId(teacherRecurringAvailability.getId());
        response.setDayOfWeek(TeacherRecurringAvailabilityResponse.DayOfWeekEnum.fromValue(teacherRecurringAvailability.getDayOfWeek().getValue()));
        response.setStartTime(teacherRecurringAvailability.getStartTime().toString());
        if (LocalTime.of(23, 59, 59).equals(teacherRecurringAvailability.getEndTime())) {
            response.setEndTime("24:00");
        } else {
            response.setEndTime(teacherRecurringAvailability.getEndTime().toString());
        }
        return response;
    }

    // Exception availability
    default AddTeacherExceptionAvailability toAddTeacherExceptionAvailability(int teacherId, AddTeacherExceptionAvailabilityRequest request) {
        AddTeacherExceptionAvailability exceptionAvailability = new AddTeacherExceptionAvailability();
        exceptionAvailability.setTeacherId(teacherId);
        exceptionAvailability.setExceptionDate(request.getExceptionDate());
        exceptionAvailability.setStartTime(LocalTime.parse(request.getStartTime()));
        if ("24:00".equals(request.getEndTime())) {
            exceptionAvailability.setEndTime(LocalTime.parse("23:59:59"));
        } else {
            exceptionAvailability.setEndTime(LocalTime.parse(request.getEndTime()));
        }
        return exceptionAvailability;
    }

    default TeacherExceptionAvailabilitiesResponse toTeacherExceptionAvailabilitiesResponse(List<TeacherExceptionAvailability> teacherExceptionAvailabilities) {
        List<TeacherExceptionAvailabilityResponse> exceptionAvailabilities = teacherExceptionAvailabilities.stream()
            .map(this::toTeacherExceptionAvailabilityResponse)
            .toList();
        TeacherExceptionAvailabilitiesResponse response = new TeacherExceptionAvailabilitiesResponse();
        response.setExceptionAvailabilities(exceptionAvailabilities);
        return response;
    }

    default TeacherExceptionAvailabilityResponse toTeacherExceptionAvailabilityResponse(TeacherExceptionAvailability exceptionAvailability) {
        TeacherExceptionAvailabilityResponse response = new TeacherExceptionAvailabilityResponse();
        response.setId(exceptionAvailability.getId());
        response.setExceptionDate(exceptionAvailability.getExceptionDate());
        response.setStartTime(exceptionAvailability.getStartTime().toString());
        if (LocalTime.of(23, 59, 59).equals(exceptionAvailability.getEndTime())) {
            response.setEndTime("24:00");
        } else {
            response.setEndTime(exceptionAvailability.getEndTime().toString());
        }
        return response;
    }

    // Unavailable date
    default TeacherUnavailableDatesResponse toTeacherUnavailableDatesResponse(List<TeacherUnavailableDate> teacherUnavailableDates) {
        List<TeacherUnavailableDateResponse> unavailableDates = teacherUnavailableDates.stream()
            .map(this::toTeacherUnavailableDateResponse)
            .toList();
        TeacherUnavailableDatesResponse response = new TeacherUnavailableDatesResponse();
        response.setUnavailableDates(unavailableDates);
        return response;
    }

    TeacherUnavailableDateResponse toTeacherUnavailableDateResponse(TeacherUnavailableDate teacherUnavailableDate);
}
