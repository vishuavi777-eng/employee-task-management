package com.vishwambhar.microservices.task_service.messaging.callback;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitPublisherCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {
    private final Logger log = LoggerFactory.getLogger(RabbitPublisherCallback.class);

    @Override
    public void confirm(@Nullable CorrelationData correlationData, boolean ack, @Nullable String cause) {
        String eventId = correlationData != null ? correlationData.getId() : "unknown";

        if (!ack) {
            log.error(
                    "RabbitMQ publisher NACK received. eventId={}, cause={}",
                    eventId,
                    cause
            );
            return;
        }

        if (correlationData != null
                && correlationData.getReturned() != null) {

            log.warn(
                    "RabbitMQ accepted the message but routing failed. eventId={}",
                    eventId
            );

            return;
        }

        log.info(
                "RabbitMQ publisher ACK received and message was routable. eventId={}",
                eventId
        );
    }

    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.error(
                """
                RabbitMQ returned an unroutable message.
                exchange={}
                routingKey={}
                replyCode={}
                replyText={}
                messageId={}
                """,
                returned.getExchange(),
                returned.getRoutingKey(),
                returned.getReplyCode(),
                returned.getReplyText(),
                returned.getMessage()
                        .getMessageProperties()
                        .getMessageId()
        );
    }
}
