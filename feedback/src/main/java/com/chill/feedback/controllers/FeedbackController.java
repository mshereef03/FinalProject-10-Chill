package com.chill.feedback.controllers;

import com.chill.feedback.models.Feedback;
import com.chill.feedback.models.Review;
import com.chill.feedback.models.Thread;
import com.chill.feedback.models.Complaint;
import com.chill.feedback.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public Feedback createFeedback(@RequestBody Feedback feedback) {
        return feedbackService.createFeedback(feedback);
    }

    @GetMapping
    public List<Feedback> getAllFeedback() {
        return feedbackService.getAllOfType(Feedback.class);
    }


    @GetMapping("/{id}")
    public Feedback getFeedbackById(@PathVariable UUID id) {
        return feedbackService.getFeedbackById(id);
    }

    @PutMapping("/{id}")
    public Feedback updateFeedback(@RequestBody Feedback feedback) {
        return feedbackService.updateFeedback(feedback);
    }

    @DeleteMapping("/{id}")
    public Feedback deleteFeedback(@PathVariable UUID id) {
        return feedbackService.deleteFeedbackById(id);
    }

    @GetMapping("/review")
    public List<Review> getAllReviews() {
        return feedbackService.getAllOfType(Review.class);
    }

    @GetMapping("/thread")
    public List<Thread> getAllThreads() {
        return feedbackService.getAllOfType(Thread.class);
    }

    @GetMapping("/complaint")
    public List<Complaint> getAllComplaints() {
        return feedbackService.getAllOfType(Complaint.class);
    }
}
