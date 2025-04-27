package com.michael.exercise.mappers;

import java.time.LocalTime;
import java.util.List;

import org.mapstruct.Mapper;

import com.michael.exercise.dtos.AddTeacherExceptionAvailabilityRequest;
import com.michael.exercise.dtos.AddTeacherRecurringAvailabilityRequest;
import com.michael.exercise.dtos.AdminGetTeacherResponse;
import com.michael.exercise.dtos.GetTeacherAvailabilityResponse;
import com.michael.exercise.dtos.GetTeacherExceptionAvailabilityResponse;
import com.michael.exercise.dtos.GetTeacherRecurringAvailabilityResponse;
import com.michael.exercise.dtos.GetTeacherUnavailableDateResponse;
import com.michael.exercise.dtos.StudentGetTeacherResponse;
import com.michael.exercise.models.TeacherRecurringAvailability;
import com.michael.exercise.models.TeacherUnavailableDate;
import com.michael.exercise.models.AddTeacherExceptionAvailability;
import com.michael.exercise.models.AddTeacherRecurringAvailability;
import com.michael.exercise.models.DayOfWeekEnum;
import com.michael.exercise.models.Teacher;
import com.michael.exercise.models.TeacherAvailability;
import com.michael.exercise.models.TeacherExceptionAvailability;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    List<StudentGetTeacherResponse> toStudentGetTeacherResponseList(List<Teacher> teachers);

    List<AdminGetTeacherResponse> toAdminGetTeacherResponseList(List<Teacher> teachers);

    List<GetTeacherAvailabilityResponse> toGetTeacherAvailabilityResponseList(List<TeacherAvailability> teacherAvailabilities);

    List<GetTeacherRecurringAvailabilityResponse> toGetRecurringAvailabilityResponseList(List<TeacherRecurringAvailability> recurringAvailabilities);

    default GetTeacherRecurringAvailabilityResponse toGetRecurringAvailabilityResponse(TeacherRecurringAvailability recurringAvailability) {
        GetTeacherRecurringAvailabilityResponse response = new GetTeacherRecurringAvailabilityResponse();
        response.setId(recurringAvailability.getId());
        response.setDayOfWeek(GetTeacherRecurringAvailabilityResponse.DayOfWeekEnum.fromValue(recurringAvailability.getDayOfWeek().getValue()));
        response.setStartTime(recurringAvailability.getStartTime().toString());
        if (LocalTime.of(23, 59, 59).equals(recurringAvailability.getEndTime())) {
            response.setEndTime("24:00");
        } else {
            response.setEndTime(recurringAvailability.getEndTime().toString());
        }
        return response;
    }

    List<GetTeacherExceptionAvailabilityResponse> toGetTeacherExceptionAvailabilityResponseList(List<TeacherExceptionAvailability> exceptionAvailabilities);

    default GetTeacherExceptionAvailabilityResponse toGetTeacherExceptionAvailabilityResponse(TeacherExceptionAvailability exceptionAvailability) {
        GetTeacherExceptionAvailabilityResponse response = new GetTeacherExceptionAvailabilityResponse();
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

    List<GetTeacherUnavailableDateResponse> toGetTeacherUnavailableDateResponseList(List<TeacherUnavailableDate> teacherUnavailableDates);

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
