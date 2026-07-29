package com.vishwambhar.microservices.notification_service.messaging.processor;

import com.vishwambhar.microservices.notification_service.idempotency.ProcessedMessageRepository;
import com.vishwambhar.microservices.notification_service.messaging.event.TaskCreatedEvent;
import com.vishwambhar.microservices.notification_service.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskCreatedEventProcessor {

    private static final Logger log =
            LoggerFactory.getLogger(TaskCreatedEventProcessor.class);

    private static final String CONSUMER_NAME =
            "task-created-notification-consumer";

    private final ProcessedMessageRepository processedMessageRepository;
    private final NotificationService notificationService;

    public TaskCreatedEventProcessor(
            ProcessedMessageRepository processedMessageRepository,
            NotificationService notificationService
    ) {
        this.processedMessageRepository = processedMessageRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public void process(TaskCreatedEvent event) {

        int insertedRows =
                processedMessageRepository.claimMessage(
                        CONSUMER_NAME,
                        event.eventId()
                );

        if (insertedRows == 0) {
            log.warn(
                    "Duplicate TaskCreatedEvent skipped. eventId={}, consumer={}",
                    event.eventId(),
                    CONSUMER_NAME
            );

            return;
        }

        notificationService.sendTaskCreatedNotification(event);

        log.info(
                "TaskCreatedEvent processed successfully. eventId={}, consumer={}",
                event.eventId(),
                CONSUMER_NAME
        );
    }
}