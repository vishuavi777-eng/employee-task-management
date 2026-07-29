package com.vishwambhar.microservices.notification_service.service;

import com.vishwambhar.microservices.notification_service.messaging.event.TaskCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class NotificationService {

    private static final Logger log =
            LoggerFactory.getLogger(NotificationService.class);

    public void sendTaskCreatedNotification(TaskCreatedEvent event) {
        this.triggerNotification(event);
    }

    private void triggerNotification(TaskCreatedEvent event) {
        String message =
                "A new task has been assigned: " + event.title();

        log.info(
                "Notification sent. employeeId={}, taskId={}, message={}",
                event.employeeId(),
                event.taskId(),
                message
        );
    }



    /*
    * Testing Permanent Failure and Attempt Based Failures
    * */
    ConcurrentHashMap<String, AtomicInteger> attempts = new ConcurrentHashMap<>();

    public void sendTaskCreatedNotificationWithAttempt(TaskCreatedEvent event) {
        int currentAttempts = attempts.computeIfAbsent(event.eventId(),
                key->new AtomicInteger()).incrementAndGet();

        log.info(
                "Sending notification. eventId={}, attempt={}",
                event.eventId(),
                currentAttempts
        );

        if (currentAttempts < 3) {
            throw new RuntimeException(
                    "Simulated temporary notification failure"
            );
        }

        this.triggerNotification(event);
    }

    public void sendTaskCreatedNotificationPermanentFailure(
            TaskCreatedEvent event
    ) {
        log.error(
                "Simulating permanent failure. eventId={}, taskId={}",
                event.eventId(),
                event.taskId()
        );

        throw new RuntimeException(
                "Simulated permanent notification failure"
        );
    }
}