package com.michael.exercise.models;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class Teacher {
    private int id;
    private String name;
    private String phone;
    private String email;
    private OffsetDateTime createdAt;
}
