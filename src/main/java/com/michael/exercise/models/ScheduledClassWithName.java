package com.michael.exercise.models;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class ScheduledClassWithName {
    private int id;
    private int studentId;
    private String studentName;
    private int teacherId;
    private String teacherName;
    private LocalDate classDate;
    private LocalTime startTime;
}
