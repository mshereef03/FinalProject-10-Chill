package com.chill.feedback.rabbitmq;

import com.chill.feedback.models.*;
import com.chill.feedback.models.Thread;
import com.chill.feedback.repositories.FeedbackRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class FeedbackListener {

    private final FeedbackRepository feedbackRepo;

    public FeedbackListener(FeedbackRepository feedbackRepo) {
        this.feedbackRepo = feedbackRepo;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handleFeedback(@Payload Map<String,Object> payload) {
        // 1️⃣ pull out the fields you know you'll get
        String type     = ((String) payload.get("type")).toLowerCase();
        UUID   userId   = UUID.fromString((String) payload.get("userId"));
        UUID   vendorId = UUID.fromString((String) payload.get("vendorId"));
        UUID   orderId  = UUID.fromString((String) payload.get("orderId"));
        String comment  = (String) payload.get("comment");

        // 2️⃣ build the right Feedback subtype
        Feedback feedback;
        switch (type) {
            case "review":
                Review review = new Review();
                review.setUserId(userId);
                review.setVendorId(vendorId);
                review.setOrderId(orderId);
                review.setComment(comment);
                feedback = review;
                break;

            case "complaint":
                Complaint complaint = new Complaint();
                complaint.setUserId(userId);
                complaint.setVendorId(vendorId);
                complaint.setOrderId(orderId);
                complaint.setComment(comment);
                feedback = complaint;
                break;

            case "thread":
                Thread thread = new Thread();
                thread.setUserId(userId);
                thread.setVendorId(vendorId);
                thread.setOrderId(orderId);
                thread.setComment(comment);
                feedback = thread;
                break;

            default:
                throw new IllegalArgumentException("Unknown feedback type: " + type);
        }

        // 3️⃣ save it
        feedbackRepo.save(feedback);
    }
}
