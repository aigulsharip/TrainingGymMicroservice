package com.example.traininggymmicroservice.rabbitmq;

import dto.TrainingDTO;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {


    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();
        messageConverter.setClassMapper(classMapper());
        return messageConverter;
    }

    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("java.util.ArrayList", ArrayList.class);
        idClassMapping.put("dto.TrainingDTO", TrainingDTO.class);
        classMapper.setIdClassMapping(idClassMapping);
        return classMapper;
    }

    @Bean
    public Queue deadLetterQueue(){
        return QueueBuilder.durable("trainings_queue.dlq").build();
    }

    @Bean
    public TopicExchange deadLetterExchange(){
        return ExchangeBuilder.topicExchange("dlx").durable(true).build();
    }

    @Bean
    public Binding DLQbinding(){
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with("dlx.trainings");
    }

}
