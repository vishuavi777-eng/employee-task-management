package com.vishwambhar.microservices.notification_service.messaging.consumer;

import com.vishwambhar.microservices.notification_service.messaging.RabbitMqNames;
import com.vishwambhar.microservices.notification_service.messaging.event.TaskCreatedEvent;
import com.vishwambhar.microservices.notification_service.messaging.processor.TaskCreatedEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TaskCreatedEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(TaskCreatedEventConsumer.class);

    private final TaskCreatedEventProcessor eventProcessor;

    public TaskCreatedEventConsumer(
            TaskCreatedEventProcessor eventProcessor
    ) {
        this.eventProcessor = eventProcessor;
    }

    @RabbitListener(
            queues = RabbitMqNames.TASK_CREATED_QUEUE
    )
    public void consume(TaskCreatedEvent event) {

        String threadName = Thread.currentThread().getName();
        log.info(
                "TaskCreatedEvent received. threadName={}, eventId={}, taskId={}, employeeId={}",
                threadName,
                event.eventId(),
                event.taskId(),
                event.employeeId()
        );

        eventProcessor.process(event);

        log.info(
                "TaskCreatedEvent listener completed. eventId={}",
                event.eventId()
        );
    }
}