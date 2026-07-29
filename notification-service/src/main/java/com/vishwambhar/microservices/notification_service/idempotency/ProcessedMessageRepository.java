package com.vishwambhar.microservices.notification_service.idempotency;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProcessedMessageRepository
        extends JpaRepository<ProcessedMessage, ProcessedMessageId> {

    @Modifying
    @Query(
            value = """
                    INSERT IGNORE INTO processed_message (
                        consumer_name,
                        event_id,
                        processed_at
                    )
                    VALUES (
                        :consumerName,
                        :eventId,
                        CURRENT_TIMESTAMP
                    )
                    """,
            nativeQuery = true
    )
    int claimMessage(
            String consumerName,
            String eventId
    );
}
