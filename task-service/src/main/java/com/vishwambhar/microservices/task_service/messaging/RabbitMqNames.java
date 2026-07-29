package com.vishwambhar.microservices.task_service.messaging;

public final class RabbitMqNames {

    private RabbitMqNames() {
    }

    public static final String TASK_EVENTS_EXCHANGE =
            "task.events.exchange";

    public static final String TASK_CREATED_QUEUE =
            "notification.task-created.queue";

    public static final String TASK_CREATED_ROUTING_KEY =
            "task.created";
}