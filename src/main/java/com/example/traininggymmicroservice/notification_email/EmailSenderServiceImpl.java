package com.example.traininggymmicroservice.notification_email;

import dto.TrainingDTO;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {


    private final JavaMailSender mailSender;

    public EmailSenderServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    @Override
    public void sendEmail(String to, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("sharipaigul@gmail.com");
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
        System.out.println("Mail Sent Succesfully");

    }

    @Override
    public EmailMessage renderTrainingTraineeEmail(TrainingDTO trainingDTO) {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setTo(trainingDTO.getTraineeEmail());
        emailMessage.setSubject("Notification: you are appointed for new training session");
        String message = "";
        message += "Good day, " + trainingDTO.getTraineeName() + "! \n \n";
        message += "You will have training with " + trainingDTO.getTrainerName() + " on " + trainingDTO.getTrainingDate() + "\n";
        message += " You will have  " + trainingDTO.getTrainingType() + " lasting for  " + trainingDTO.getTrainingDuration()  + " minutes \n \n";
        message += " If you want to cancel or reschedule training session, please let us know at least 4 hours before training" ;


        emailMessage.setMessage(message);
        return emailMessage;
    }

    @Override
    public EmailMessage renderTrainingTrainerEmail(TrainingDTO trainingDTO) {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setTo(trainingDTO.getTrainerEmail());
        emailMessage.setSubject("Notification: new training session");
        String message = "";
        message += "Good day, trainer " + trainingDTO.getTrainerName() + "! \n \n";
        message += "You will have training with " + trainingDTO.getTraineeName() + " on " + trainingDTO.getTrainingDate() + "\n";
        message += " You will have  " + trainingDTO.getTrainingType() + " lasting for  " + trainingDTO.getTrainingDuration()  + " minutes \n \n";
        message += " You can directly contact trainee via his/her email " + trainingDTO.getTraineeEmail();
        emailMessage.setMessage(message);
        return emailMessage;
    }


}
