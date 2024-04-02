package com.example.traininggymmicroservice.repository;

import com.example.traininggymmicroservice.entity.TrainerWorkload;
import com.example.traininggymmicroservice.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerWorkloadRepository extends JpaRepository<TrainerWorkload, Long> {
}
