package com.example.traininggymmicroservice.activeMQ;

import dto.TrainingInfo;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class ActiveMQListener {

    @JmsListener(destination = "training.info.active.queue")
    public void receive(TrainingInfo trainingInfo) {
        System.out.println("Training information received = " + trainingInfo);
    }
}