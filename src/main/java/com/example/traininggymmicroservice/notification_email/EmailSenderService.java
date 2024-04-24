package com.example.traininggymmicroservice.notification_email;

import dto.TrainingDTO;

public interface EmailSenderService {
    void sendEmail(String to, String subject, String message);

    EmailMessage renderTrainingTraineeEmail (TrainingDTO trainingDTO);

    EmailMessage renderTrainingTrainerEmail (TrainingDTO trainingDTO);


}