package com.example.traininggymmicroservice.repository;

import com.example.traininggymmicroservice.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
    List<Training> findByTrainee(String trainee);
}

