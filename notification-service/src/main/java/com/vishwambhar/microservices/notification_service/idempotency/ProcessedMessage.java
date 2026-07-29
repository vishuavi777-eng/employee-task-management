package com.vishwambhar.microservices.notification_service.idempotency;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "processed_message")
public class ProcessedMessage {

    @EmbeddedId
    private ProcessedMessageId id;

    @Column(
            name = "processed_at",
            nullable = false,
            updatable = false
    )
    private Instant processedAt;

    protected ProcessedMessage() {
    }

    public ProcessedMessage(
            ProcessedMessageId id,
            Instant processedAt
    ) {
        this.id = id;
        this.processedAt = processedAt;
    }

    public ProcessedMessageId getId() {
        return id;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }
}