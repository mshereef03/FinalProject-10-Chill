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
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAllOfType(Feedback.class);
    }

    @GetMapping("/reviews")
    public List<Review> getAllReviews() {
        return feedbackService.getAllOfType(Review.class);
    }

    @GetMapping("/reviews/vendor/{vendorId}/top")
    public List<Review> getTopReviewsForVendor(@PathVariable("vendorId") UUID vendorId) {
        return feedbackService.getTopReviewsForVendor(vendorId);
    }

    @GetMapping("/threads")
    public List<Thread> getAllThreads() {
        return feedbackService.getAllOfType(Thread.class);
    }

    @GetMapping("/complaints")
    public List<Complaint> getAllComplaints() {
        return feedbackService.getAllOfType(Complaint.class);
    }

    @GetMapping("/complaints/vendor/{vendorId}")
    public List<Complaint> getComplaintsForVendor(@PathVariable UUID vendorId) {
        return feedbackService.getComplaintsForVendor(vendorId);
    }


    @GetMapping("/complaints/user/{userId}")
    public List<Complaint> getComplaintsForUser(@PathVariable UUID userId) {
        return feedbackService.getComplaintsForUser(userId);
    }


    @GetMapping("/{id}")
    public Feedback getFeedbackById(@PathVariable UUID id) {
        return feedbackService.getFeedbackById(id);
    }



    @PutMapping("/{id}")
    public Feedback updateFeedback(
            @PathVariable UUID id,
            @RequestBody  Feedback feedback
    ) {
        feedback.setId(id);
        return feedbackService.updateFeedback(feedback);
    }

    @DeleteMapping("/{id}")
    public Feedback deleteFeedback(@PathVariable UUID id) {
        return feedbackService.deleteFeedbackById(id);
    }

    @PostMapping("/{id}/upvote")
    public Feedback upvoteFeedback(@PathVariable UUID id, @RequestHeader("X-User-Id") UUID userId) {
        return feedbackService.upvoteFeedback(id,userId);
    }

    @PostMapping("/{id}/downvote")
    public Feedback downvoteFeedback(@PathVariable UUID id, @RequestHeader("X-User-Id") UUID userId) {
        return feedbackService.downvoteFeedback(id,userId);
    }


    @PostMapping("/{id}/reply")
    public Feedback replyToFeedback(
            @PathVariable("id") UUID parentId,
            @RequestBody Feedback replyPayload
    ) {
        return feedbackService.replyToFeedback(parentId, replyPayload);
    }






}
