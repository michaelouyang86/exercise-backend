package com.michael.exercise.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import lombok.Data;

@Data
public class TeacherAvailability {
    private LocalDate date;
    private String dayOfWeek;
    private Set<LocalTime> timeslots;
}
