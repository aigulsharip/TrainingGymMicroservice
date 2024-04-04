package com.example.traininggymmicroservice.repository;

import com.example.traininggymmicroservice.entity.TrainerWorkload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface TrainerWorkloadRepository extends JpaRepository<TrainerWorkload, Long> {

    void deleteTrainerWorkloadByUsernameAndTrainingDurationAndTrainingDate(String username, int trainingDuration, LocalDate trainingDate);

}
