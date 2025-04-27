package com.michael.exercise.models;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class Student {
    private int id;
    private String name;
    private String phone;
    private String email;
    private OffsetDateTime createdAt;
}
