package com.vishwambhar.microservices.notification_service.service;

import com.vishwambhar.microservices.notification_service.messaging.event.TaskCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log =
            LoggerFactory.getLogger(NotificationService.class);

    public void sendTaskCreatedNotification(
            TaskCreatedEvent event
    ) {

//        throw new RuntimeException(
//                "Simulated notification failure"
//        );

        String message =
                "A new task has been assigned: " + event.title();

        log.info(
                "Notification sent. employeeId={}, taskId={}, message={}",
                event.employeeId(),
                event.taskId(),
                message
        );
    }
}