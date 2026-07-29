package com.vishwambhar.microservices.notification_service.messaging;

public final class RabbitMqNames {

    private RabbitMqNames() {
    }

    public static final String TASK_EVENTS_EXCHANGE =
            "task.events.exchange";

    public static final String TASK_CREATED_QUEUE =
            "notification.task-created.queue";

    public static final String TASK_CREATED_ROUTING_KEY =
            "task.created";


    public static final String TASK_CREATED_DEAD_LETTER_EXCHANGE =
            "notification.task-created.dlx";

    public static final String TASK_CREATED_DEAD_LETTER_QUEUE =
            "notification.task-created.dlq";

    public static final String TASK_CREATED_DEAD_LETTER_ROUTING_KEY =
            "notification.task-created.dead";
}