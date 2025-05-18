package com.chill.feedback.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {
    public static final String QUEUE = "feedback_queue";
    public static final String EXCHANGE = "shared_exchange";
    public static final String ROUTING_KEY = "feedback_routing_key";

    // — declare queue, exchange, binding —
    @Bean
    public Queue feedbackQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public TopicExchange feedbackExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Binding feedbackBinding(Queue feedbackQueue, TopicExchange feedbackExchange) {
        return BindingBuilder
                .bind(feedbackQueue)
                .to(feedbackExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jacksonConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // — listener container factory that uses your converter —
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf,
            Jackson2JsonMessageConverter jacksonConverter
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cf);
        factory.setMessageConverter(jacksonConverter);
        return factory;
    }


}
