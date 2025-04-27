package com.michael.exercise.models;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class ScheduledClass {
    private int id;
    private int studentId;
    private int teacherId;
    private LocalDate classDate;
    private LocalTime startTime;
}
