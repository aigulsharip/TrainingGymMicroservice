package com.example.traininggymmicroservice.controller;

import com.example.traininggymmicroservice.entity.TrainerWorkload;
import com.example.traininggymmicroservice.entity.TrainerWorkloadRequest;
import com.example.traininggymmicroservice.entity.Training;
import com.example.traininggymmicroservice.repository.TrainerWorkloadRepository;
import com.example.traininggymmicroservice.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingRepository trainingRepository;

    @Autowired
    private TrainerWorkloadRepository trainerWorkloadRepository;

    @GetMapping("/status-check")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello from secured endpoint of Training Microservice");
    }

    @GetMapping
    public ResponseEntity<List<Training>> getAllTrainings() {
        List<Training> trainings = trainingRepository.findAll();
        if (!trainings.isEmpty()) {
            return ResponseEntity.ok(trainings);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/trainer/workload")
    public ResponseEntity<String> updateWorkload(@RequestBody TrainerWorkload request) {
        trainerWorkloadRepository.save(request);
        return ResponseEntity.ok("Workload updated successfully");
    }
}
