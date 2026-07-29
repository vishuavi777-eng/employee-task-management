package com.vishwambhar.microservices.notification_service.idempotency;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProcessedMessageId implements Serializable {

    @Column(
            name = "consumer_name",
            nullable = false,
            length = 100
    )
    private String consumerName;

    @Column(
            name = "event_id",
            nullable = false,
            length = 100
    )
    private String eventId;

    protected ProcessedMessageId() {
    }

    public ProcessedMessageId(
            String consumerName,
            String eventId
    ) {
        this.consumerName = consumerName;
        this.eventId = eventId;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public String getEventId() {
        return eventId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof ProcessedMessageId that)) {
            return false;
        }

        return Objects.equals(consumerName, that.consumerName)
                && Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(consumerName, eventId);
    }
}