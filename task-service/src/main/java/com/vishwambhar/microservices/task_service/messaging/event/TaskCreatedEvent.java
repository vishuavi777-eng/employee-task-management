package com.vishwambhar.microservices.task_service.messaging.event;

import java.time.Instant;

public record TaskCreatedEvent(
        String eventId,
        Long taskId,
        Long employeeId,
        String title,
        Instant occurredAt
) {
}
