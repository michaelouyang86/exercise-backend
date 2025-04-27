package com.michael.exercise.models;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class User {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String password;
    private String role; // "STUDENT" or "TEACHER"
    private OffsetDateTime createdAt;
}
