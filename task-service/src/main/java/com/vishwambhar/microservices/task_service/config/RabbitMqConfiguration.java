package com.vishwambhar.microservices.task_service.config;

import com.vishwambhar.microservices.task_service.messaging.RabbitMqNames;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitMqConfiguration {

    @Bean
    public MessageConverter rabbitMessageConverter() {
        return new JacksonJsonMessageConverter(
                "com.vishwambhar.microservices"
        );
    }

    @Bean
    public TopicExchange taskEventsExchange() {
        return new TopicExchange(
                RabbitMqNames.TASK_EVENTS_EXCHANGE,
                true,
                false
        );
    }

    @Bean
    public Queue taskCreatedQueue() {
        return QueueBuilder
                .durable(RabbitMqNames.TASK_CREATED_QUEUE)
                .build();
    }

    @Bean
    public Binding taskCreatedBinding(
            Queue taskCreatedQueue,
            TopicExchange taskEventsExchange
    ) {
        return BindingBuilder
                .bind(taskCreatedQueue)
                .to(taskEventsExchange)
                .with(RabbitMqNames.TASK_CREATED_ROUTING_KEY);
    }
}