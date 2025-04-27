package com.michael.exercise.models;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class Admin {
    private int id;
    private String username;
    private String password;
    private OffsetDateTime createdAt;
}
