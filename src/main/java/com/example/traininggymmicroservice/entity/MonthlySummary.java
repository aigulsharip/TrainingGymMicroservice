package com.example.traininggymmicroservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlySummary {
    private int month;
    private int year;
    private int duration;

    public MonthlySummary(int month, int totalDuration) {
        this.month = month;
        this.duration = totalDuration;
    }
}
