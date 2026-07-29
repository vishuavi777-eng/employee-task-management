package com.vishwambhar.microservices.notification_service.messaging.consumer;

import com.vishwambhar.microservices.notification_service.messaging.RabbitMqNames;
import com.vishwambhar.microservices.notification_service.messaging.event.TaskCreatedEvent;
import com.vishwambhar.microservices.notification_service.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TaskCreatedEventConsumer {
    private final Logger log = LoggerFactory.getLogger(TaskCreatedEventConsumer.class);
    private final NotificationService notificationService;

    public TaskCreatedEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = RabbitMqNames.TASK_CREATED_QUEUE)
    public void consume(TaskCreatedEvent event) {
        log.info(
                """
                TaskCreatedEvent received:
                eventId={}
                taskId={}
                employeeId={}
                title={}
                occurredAt={}
                """,
                event.eventId(),
                event.taskId(),
                event.employeeId(),
                event.title(),
                event.occurredAt()
        );

        notificationService.sendTaskCreatedNotification(event);

        log.info(
                "TaskCreatedEvent processed successfully. eventId={}",
                event.eventId()
        );
    }
}
