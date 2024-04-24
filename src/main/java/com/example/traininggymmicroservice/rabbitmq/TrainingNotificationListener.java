package com.example.traininggymmicroservice.rabbitmq;

import com.example.traininggymmicroservice.notification_email.EmailMessage;
import com.example.traininggymmicroservice.notification_email.EmailSenderService;
import dto.TrainingDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingNotificationListener {

    private final EmailSenderService emailSenderService;
    private final RabbitTemplate rabbitTemplate;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "training_fitness_queue",
                arguments = {
                    @Argument(name = "x-dead-letter-exchange", value = "dlx"),
                    @Argument(name = "x-dead-letter-routing-key", value = "dlx.trainings")
                }
            ),
            exchange = @Exchange(value = "${mq.training.topic.exchange}",
                    type = ExchangeTypes.TOPIC),
            key = "training.#"
    ))
    public void createFitnessTraining(TrainingDTO trainingDTO){
        log.info("Created training  : {}", trainingDTO);
        try{
            processIncompleteTraining(trainingDTO);
        }catch (Exception e){
            log.error("Error on processing training : {}, Error : {}", trainingDTO, e.getMessage());
            throw e;
        }

        // Process the valid training
        processValidTraining(trainingDTO);

    }

    private boolean isInvalid(TrainingDTO trainingDTO) {
        // Check if any required information is missing
        return trainingDTO.getTraineeName() == null ||
                trainingDTO.getTrainerName() == null ||
                trainingDTO.getTraineeEmail() == null ||
                trainingDTO.getTrainerEmail() == null ||
                trainingDTO.getTrainingType() == null ||
                trainingDTO.getTrainingDate() == null ||
                trainingDTO.getTrainingDuration() <= 0;
    }

    private void processValidTraining(TrainingDTO trainingDTO) {
        // Process the valid training
        System.out.println("Processing training: " + trainingDTO);
    }

    private void processIncompleteTraining(TrainingDTO trainingDTO){
        if (isInvalid(trainingDTO)) {
            throw new RuntimeException("Failed to process incomplete training");
        }
    }

    private void processFailTraining(TrainingDTO trainingDTO){
        if(true){
            throw new RuntimeException("Failed to process training");
        }
        /*
        try{
            processFailTraining(trainingDTO);
        }catch (Exception e){
            log.error("Error on processing training : {}, Error : {}", trainingDTO, e.getMessage());
            throw e;
        }
         */


    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "training_notification_queue"
            ),
            exchange = @Exchange(value = "${mq.trainee.notification.topic.exchange}",
                    type = ExchangeTypes.TOPIC),
            key = "trainee.#"
    ))
    public void consumeTrainingInfo(TrainingDTO trainingDTO) {
        EmailMessage emailMessage = emailSenderService.renderTrainingTraineeEmail(trainingDTO);
        emailSenderService.sendEmail(emailMessage.getTo(), emailMessage.getSubject(), emailMessage.getMessage());
        log.info("Message: {} successfully consumed", trainingDTO);

    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "training_trainer_notification_queue"
            ),
            exchange = @Exchange(value = "${mq.trainer.notification.topic.exchange}",
                    type = ExchangeTypes.TOPIC),
            key = "trainer.#"
    ))
    public void consumeTrainingInfoForTrainerNotification(TrainingDTO trainingDTO) {
        EmailMessage emailMessage = emailSenderService.renderTrainingTrainerEmail(trainingDTO);
        emailSenderService.sendEmail(emailMessage.getTo(), emailMessage.getSubject(), emailMessage.getMessage());
        log.info("Message: {} successfully consumed", trainingDTO);

    }




}