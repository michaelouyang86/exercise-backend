package com.michael.exercise.models;

import lombok.Data;

@Data
public class StudentPointsRecord {
    private int id;
    private int studentId;
    private int adjustedPoints;
    private String reason;
    private int pointsAfterAdjustment;
}
