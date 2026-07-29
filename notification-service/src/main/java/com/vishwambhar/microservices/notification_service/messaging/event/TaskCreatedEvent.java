package com.vishwambhar.microservices.notification_service.messaging.event;

import java.time.Instant;

public record TaskCreatedEvent(
        String eventId,
        Long taskId,
        Long employeeId,
        String title,
        Instant occurredAt
) {
}