package com.vishwambhar.microservices.task_service.messaging.publisher;

import com.vishwambhar.microservices.task_service.messaging.RabbitMqNames;
import com.vishwambhar.microservices.task_service.messaging.event.TaskCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class TaskEventPublisher {
    private final Logger log = LoggerFactory.getLogger(TaskEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public TaskEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishTaskCreated(TaskCreatedEvent event) {

        log.info(
                "Publishing TaskCreatedEvent. eventId={}, taskId={}, exchange={}, routingKey={}",
                event.eventId(),
                event.taskId(),
                RabbitMqNames.TASK_EVENTS_EXCHANGE,
                RabbitMqNames.TASK_CREATED_ROUTING_KEY
        );

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMqNames.TASK_EVENTS_EXCHANGE,
                    RabbitMqNames.TASK_CREATED_ROUTING_KEY,
                    event
            );

            log.info(
                    "TaskCreatedEvent publish request completed. eventId={}, taskId={}",
                    event.eventId(),
                    event.taskId()
            );

        } catch (Exception exception) {
            log.error(
                    "Failed to publish TaskCreatedEvent. eventId={}, taskId={}, exchange={}, routingKey={}",
                    event.eventId(),
                    event.taskId(),
                    RabbitMqNames.TASK_EVENTS_EXCHANGE,
                    RabbitMqNames.TASK_CREATED_ROUTING_KEY,
                    exception
            );

            throw exception;
        }
    }
}
