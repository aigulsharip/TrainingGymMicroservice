package com.example.traininggymmicroservice.service;

import com.example.traininggymmicroservice.entity.MonthlySummary;
import com.example.traininggymmicroservice.entity.TrainerSummary;
import com.example.traininggymmicroservice.entity.TrainerWorkload;
import com.example.traininggymmicroservice.repository.TrainerWorkloadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainerWorkloadService {

    private final TrainerWorkloadRepository trainerWorkloadRepository;

    @Transactional
    public void addTraining(TrainerWorkload request) {
        trainerWorkloadRepository.save(request);
    }
   @Transactional
    public void deleteTrainingByUsernameAndDurationAndDate(String username, int trainingDuration, LocalDate trainingDate) {
        trainerWorkloadRepository.deleteTrainerWorkloadByUsernameAndTrainingDurationAndTrainingDate(username, trainingDuration, trainingDate);
    }

    public List<TrainerSummary> calculateMonthlySummary() {
        List<TrainerWorkload> allWorkloads = trainerWorkloadRepository.findAll();

        // Grouping trainings by trainer username and month

        Map<String, Map<Integer, List<TrainerWorkload>>> groupedByTrainerAndMonth = allWorkloads.stream()
                .collect(Collectors.groupingBy(
                        TrainerWorkload::getUsername,
                        Collectors.groupingBy(workload -> workload.getTrainingDate().getMonth().getValue())
                ));

        // Calculating summary for each trainer
        return groupedByTrainerAndMonth.entrySet().stream().map(entry -> {
            String trainerUsername = entry.getKey();
            Map<Integer, List<TrainerWorkload>> monthlyWorkloads = entry.getValue();

            // Calculate total duration for each month
            List<MonthlySummary> monthlySummaries = monthlyWorkloads.entrySet().stream().map(monthEntry -> {
                int month = monthEntry.getKey();
                List<TrainerWorkload> monthWorkloads = monthEntry.getValue();
                int totalDuration = monthWorkloads.stream().mapToInt(TrainerWorkload::getTrainingDuration).sum();
                return new MonthlySummary(month, totalDuration);
            }).collect(Collectors.toList());

            // Calculate total duration for all months
            int totalDuration = monthlySummaries.stream().mapToInt(MonthlySummary::getDuration).sum();

            // Get trainer details
            TrainerWorkload firstWorkload = allWorkloads.stream().filter(workload ->
                    workload.getUsername().equals(trainerUsername)).findFirst().orElse(null);
            String firstName = firstWorkload != null ? firstWorkload.getFirstName() : null;
            String lastName = firstWorkload != null ? firstWorkload.getLastName() : null;
            Boolean status = firstWorkload != null ? firstWorkload.getIsActive() : null;

            // Extract years from trainings
            List<Integer> years = allWorkloads.stream()
                    .map(workload -> workload.getTrainingDate().getYear())
                    .distinct()
                    .collect(Collectors.toList());

            return new TrainerSummary(trainerUsername, firstName, lastName, status, years, monthlySummaries, totalDuration);
        }).collect(Collectors.toList());
    }

}
