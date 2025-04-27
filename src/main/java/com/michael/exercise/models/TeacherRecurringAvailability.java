package com.michael.exercise.models;

import java.time.LocalTime;

import lombok.Data;

@Data
public class TeacherRecurringAvailability {
    private int id;
    private int teacherId;
    private DayOfWeekEnum dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}
