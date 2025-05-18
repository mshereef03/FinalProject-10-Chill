package com.chill.feedback.service;

import com.chill.feedback.models.Feedback;
import com.chill.feedback.rabbitmq.RabbitMQConfig;
import com.chill.feedback.repositories.FeedbackRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @RabbitListener(queues = RabbitMQConfig.FEEDBACK_QUEUE)
    public void receiveFeedbackMessage(Feedback feedback) {
        // 'feedback' is already the correct subclass deserialized by Jackson

        feedbackRepository.save(feedback);

        System.out.println("Feedback created successfully with ID: " + feedback.getId());
    }
}
