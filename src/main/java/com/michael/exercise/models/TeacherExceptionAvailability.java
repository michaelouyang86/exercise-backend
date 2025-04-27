package com.michael.exercise.models;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class TeacherExceptionAvailability {
    private int id;
    private int teacherId;
    private LocalDate exceptionDate;
    private LocalTime startTime;
    private LocalTime endTime;
}
