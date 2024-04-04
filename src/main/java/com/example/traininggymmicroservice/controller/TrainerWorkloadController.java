package com.example.traininggymmicroservice.controller;

import com.example.traininggymmicroservice.entity.ActionType;
import com.example.traininggymmicroservice.entity.TrainerSummary;
import com.example.traininggymmicroservice.entity.TrainerWorkload;
import com.example.traininggymmicroservice.repository.TrainerWorkloadRepository;
import com.example.traininggymmicroservice.service.TrainerWorkloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/trainings")
@RequiredArgsConstructor
@Slf4j
public class TrainerWorkloadController {

    private final TrainerWorkloadRepository trainerWorkloadRepository;
    private final TrainerWorkloadService trainerWorkloadService;


    @GetMapping("/status-check")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello from secured endpoint of Training Microservice");
    }

    @GetMapping("/trainer/workload")
    public ResponseEntity<List<TrainerWorkload>> getAllTrainerWorkloads() {
        log.info("Received request to get all trainer workloads");
        List<TrainerWorkload> workloads = trainerWorkloadRepository.findAll();
        if (!workloads.isEmpty()) {
            log.info("Returning all trainer workloads: {}", workloads);
            return ResponseEntity.ok(workloads);
        } else {
            log.warn("No trainer workloads found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/trainer/workload")
    public ResponseEntity<String> updateWorkload(@RequestBody TrainerWorkload request) {
        log.info("Received request to update trainer workload: {}", request);

        // Check ActionType and perform the corresponding action
        if (request.getActionType() == ActionType.ADD) {
            trainerWorkloadService.addTraining(request);
        } else if (request.getActionType() == ActionType.DELETE) {
            trainerWorkloadService.deleteTrainingByUsernameAndDurationAndDate(request.getUsername(), request.getTrainingDuration(), request.getTrainingDate());
        } else {
            log.error("Invalid ActionType provided in request: {}", request.getActionType());
            return ResponseEntity.badRequest().body("Invalid ActionType");
        }

        log.info("Trainer workload updated successfully: {}", request);
        return ResponseEntity.ok("Workload updated successfully");
    }


    @GetMapping("/trainer/workload/{id}")
    public ResponseEntity<TrainerWorkload> getWorkloadById(@PathVariable Long id) {
        log.info("Received request to get trainer workload with ID: {}", id);

        Optional<TrainerWorkload> workloadOptional = trainerWorkloadRepository.findById(id);
        if (workloadOptional.isPresent()) {
            TrainerWorkload workload = workloadOptional.get();
            log.info("Trainer workload found with ID: {}", id);
            return ResponseEntity.ok().body(workload);
        } else {
            log.error("Trainer workload with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/trainer/workload/{id}")
    public ResponseEntity<Void> deleteWorkloadById(@PathVariable Long id) {
        log.info("Received request to delete trainer workload with ID: {}", id);

        if (trainerWorkloadRepository.existsById(id)) {
            trainerWorkloadRepository.deleteById(id);
            log.info("Trainer workload deleted successfully with ID: {}", id);
            return ResponseEntity.noContent().build();
        } else {
            log.error("Trainer workload with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<TrainerSummary>> getMonthlySummaries() {
        List<TrainerSummary> monthlySummaries = trainerWorkloadService.calculateMonthlySummary();
        if (!monthlySummaries.isEmpty()) {
            return ResponseEntity.ok(monthlySummaries);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
