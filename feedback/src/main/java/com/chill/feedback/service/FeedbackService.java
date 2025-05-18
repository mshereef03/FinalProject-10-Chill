//package com.chill.feedback.service;
//
//import com.chill.feedback.dtos.FeedbackDTO;
//import com.chill.feedback.models.Feedback;
//import com.chill.feedback.rabbitmq.RabbitMQConfig;
//import com.chill.feedback.repositories.FeedbackRepository;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class FeedbackService {
//
//    @Autowired
//    private FeedbackRepository feedbackRepository;
//
//    @RabbitListener(queues = RabbitMQConfig.FEEDBACK_QUEUE)
//    public void receiveFeedbackMessage(FeedbackDTO feedbackDTO) {
//        // Spring will automatically convert the FeedbackDTO from the message
//
//        // Create feedback object and save it to the database
//        Feedback feedback = new Feedback() {
//            @Override
//            public Feedback update(FeedbackDTO feedbackDTO) {
//                return null;
//            }
//        };
//        feedback.setUserId(feedbackDTO.getUserId());
//        feedback.setVendorId(feedbackDTO.getVendorId());
//        feedback.setOrderId(feedbackDTO.getOrderId());
//        feedback.setComment(feedbackDTO.getComment());
//
//        feedbackRepository.save(feedback);
//
//        System.out.println("Feedback created successfully with ID: " + feedback.getId());
//    }
//}
