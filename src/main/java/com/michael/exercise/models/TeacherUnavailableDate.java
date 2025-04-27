package com.michael.exercise.models;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TeacherUnavailableDate {
    private int id;
    private int teacherId;
    private LocalDate unavailableDate;
}
