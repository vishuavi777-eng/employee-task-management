package com.vishwambhar.microservices.notification_service.config;

import com.vishwambhar.microservices.notification_service.messaging.RabbitMqNames;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfiguration {

    @Bean
    public TopicExchange taskEventsExchange() {
        return new TopicExchange(
                RabbitMqNames.TASK_EVENTS_EXCHANGE,
                true,
                false
        );
    }

    @Bean
    public DirectExchange taskCreatedDeadLetterExchange() {
        return new DirectExchange(
                RabbitMqNames.TASK_CREATED_DEAD_LETTER_EXCHANGE,
                true,
                false
        );
    }

    @Bean
    public Queue taskCreatedQueue() {
        return QueueBuilder
                .durable(RabbitMqNames.TASK_CREATED_QUEUE)
                .deadLetterExchange(
                        RabbitMqNames.TASK_CREATED_DEAD_LETTER_EXCHANGE
                )
                .deadLetterRoutingKey(
                        RabbitMqNames.TASK_CREATED_DEAD_LETTER_ROUTING_KEY
                )
                .build();
    }

    @Bean
    public Queue taskCreatedDeadLetterQueue() {
        return QueueBuilder
                .durable(
                        RabbitMqNames.TASK_CREATED_DEAD_LETTER_QUEUE
                )
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

    @Bean
    public Binding taskCreatedDeadLetterBinding(
            Queue taskCreatedDeadLetterQueue,
            DirectExchange taskCreatedDeadLetterExchange
    ) {
        return BindingBuilder
                .bind(taskCreatedDeadLetterQueue)
                .to(taskCreatedDeadLetterExchange)
                .with(
                        RabbitMqNames.TASK_CREATED_DEAD_LETTER_ROUTING_KEY
                );
    }

    @Bean
    public MessageConverter rabbitMessageConverter() {
        return new JacksonJsonMessageConverter(
                "com.vishwambhar.microservices"
        );
    }
}

//@Configuration
//public class RabbitMqConfiguration {
//
//    @Bean
//    public TopicExchange taskEventsExchange() {
//        return new TopicExchange(
//                RabbitMqNames.TASK_EVENTS_EXCHANGE,
//                true,
//                false
//        );
//    }
//
//    @Bean
//    public Queue taskCreatedQueue() {
//        return QueueBuilder
//                .durable(RabbitMqNames.TASK_CREATED_QUEUE)
//                .build();
//    }
//
//    @Bean
//    public Binding taskCreatedBinding(
//            Queue taskCreatedQueue,
//            TopicExchange taskEventsExchange
//    ) {
//        return BindingBuilder
//                .bind(taskCreatedQueue)
//                .to(taskEventsExchange)
//                .with(RabbitMqNames.TASK_CREATED_ROUTING_KEY);
//    }
//
//    @Bean
//    public MessageConverter rabbitMessageConverter() {
//        return new JacksonJsonMessageConverter(
//                "com.vishwambhar.microservices"
//        );
//    }
//}