package com.vishwambhar.microservices.task_service.config;

import com.vishwambhar.microservices.task_service.messaging.RabbitMqNames;
import com.vishwambhar.microservices.task_service.messaging.callback.RabbitPublisherCallback;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.amqp.autoconfigure.RabbitTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public RabbitTemplateCustomizer rabbitTemplateCustomizer(RabbitPublisherCallback callback) {
        return rabbitTemplate -> {
          rabbitTemplate.setConfirmCallback(callback);
          rabbitTemplate.setReturnsCallback(callback);
          rabbitTemplate.setMandatory(true);
        };
    }
}