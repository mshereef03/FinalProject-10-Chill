package com.chill.user.messaging;

import com.chill.user.dto.FeedbackDTO;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {

    private final AmqpTemplate amqpTemplate;

    public RabbitMQProducer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    /**
     * Publish a feedback message to the shared_exchange using the feedback_routing_key.
     */
    public void sendFeedback(FeedbackDTO feedback) {
        amqpTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.FEEDBACK_ROUTING_KEY,
                feedback
        );
    }
}
