package com.michael.exercise.mappers;

import java.time.LocalTime;
import java.util.List;

import org.mapstruct.Mapper;

import com.michael.exercise.dtos.AddTeacherExceptionAvailabilityRequest;
import com.michael.exercise.dtos.AddTeacherRecurringAvailabilityRequest;
import com.michael.exercise.dtos.TeacherAvailabilityResponse;
import com.michael.exercise.dtos.TeacherExceptionAvailabilityResponse;
import com.michael.exercise.dtos.TeacherForAdminResponse;
import com.michael.exercise.dtos.TeacherForStudentResponse;
import com.michael.exercise.dtos.TeacherRecurringAvailabilityResponse;
import com.michael.exercise.dtos.TeacherUnavailableDateResponse;
import com.michael.exercise.models.TeacherRecurringAvailability;
import com.michael.exercise.models.TeacherUnavailableDate;
import com.michael.exercise.models.AddTeacherExceptionAvailability;
import com.michael.exercise.models.AddTeacherRecurringAvailability;
import com.michael.exercise.models.DayOfWeekEnum;
import com.michael.exercise.models.Teacher;
import com.michael.exercise.models.TeacherAvailability;
import com.michael.exercise.models.TeacherExceptionAvailability;
import com.michael.exercise.models.TeacherIdAndName;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    List<TeacherForStudentResponse> toTeacherForStudentResponseList(List<TeacherIdAndName> teachersIdAndName);

    List<TeacherAvailabilityResponse> toTeacherAvailabilityResponseList(List<TeacherAvailability> teacherAvailabilities);

    List<TeacherForAdminResponse> toTeacherForAdminResponseList(List<Teacher> teachers);

    List<TeacherRecurringAvailabilityResponse> toRecurringAvailabilityResponseList(List<TeacherRecurringAvailability> recurringAvailabilities);

    default TeacherRecurringAvailabilityResponse toRecurringAvailabilityResponse(TeacherRecurringAvailability recurringAvailability) {
        TeacherRecurringAvailabilityResponse response = new TeacherRecurringAvailabilityResponse();
        response.setId(recurringAvailability.getId());
        response.setDayOfWeek(TeacherRecurringAvailabilityResponse.DayOfWeekEnum.fromValue(recurringAvailability.getDayOfWeek().getValue()));
        response.setStartTime(recurringAvailability.getStartTime().toString());
        if (LocalTime.of(23, 59, 59).equals(recurringAvailability.getEndTime())) {
            response.setEndTime("24:00");
        } else {
            response.setEndTime(recurringAvailability.getEndTime().toString());
        }
        return response;
    }

    List<TeacherExceptionAvailabilityResponse> toTeacherExceptionAvailabilityResponseList(List<TeacherExceptionAvailability> exceptionAvailabilities);

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

    List<TeacherUnavailableDateResponse> toTeacherUnavailableDateResponseList(List<TeacherUnavailableDate> teacherUnavailableDates);

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
}
