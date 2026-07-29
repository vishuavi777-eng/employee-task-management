package com.vishwambhar.microservices.task_service.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqConnectionVerifier implements ApplicationRunner {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMqConnectionVerifier(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        rabbitTemplate.execute(channel -> {
            System.out.println("RabbitMQ connection established successfully.");
            return null;
        });
    }
}