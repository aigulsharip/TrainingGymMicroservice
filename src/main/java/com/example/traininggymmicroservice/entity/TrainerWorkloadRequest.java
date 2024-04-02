package com.example.traininggymmicroservice.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TrainerWorkloadRequest {
    private String username;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private LocalDate trainingDate;
    private int trainingDuration;
    private ActionType actionType;
}

enum ActionType {
    ADD,
    DELETE
}