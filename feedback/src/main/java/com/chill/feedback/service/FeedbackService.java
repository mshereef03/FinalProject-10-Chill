package com.chill.feedback;

import com.chill.feedback.rabbitmq.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    @RabbitListener(queues = RabbitMQConfig.FEEDBACK_QUEUE)
    public void processFeedback(String feedbackId) {
        System.out.println("Processing feedback with ID: " + feedbackId);
        // Perform operations like saving feedback to DB, sending notifications, etc.
    }
}
