package com.chill.feedback.controllers;

import com.chill.feedback.models.Feedback;
import com.chill.feedback.models.Review;
import com.chill.feedback.models.Thread;
import com.chill.feedback.models.Complaint;
import com.chill.feedback.services.FeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.micrometer.core.annotation.Timed;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    @Timed("endpoints.createFeedback")
    public Feedback createFeedback(@RequestBody Feedback feedback) {
        return feedbackService.createFeedback(feedback);
    }

    @GetMapping
    @Timed("endpoints.getAllFeedbacks")
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAllOfType(Feedback.class);
    }

    @GetMapping("/reviews")
    @Timed("endpoints.getAllReviews")
    public List<Review> getAllReviews() {
        return feedbackService.getAllOfType(Review.class);
    }

    @GetMapping("/threads")
    @Timed("endpoints.getAllThreads")
    public List<Thread> getAllThreads() {
        return feedbackService.getAllOfType(Thread.class);
    }

    @GetMapping("/complaints")
    @Timed("endpoints.getAllComplaints")
    public List<Complaint> getAllComplaints() {
        return feedbackService.getAllOfType(Complaint.class);
    }

    @GetMapping("/reviews/user/{userId}/")
    @Timed("endpoints.getReviewsByUser")
    public List<Review> getReviewsByUser(@PathVariable("userId") UUID userId) {
        return feedbackService.getReviewsByUser(userId);
    }

    @GetMapping("/reviews/user/{userId}/top")
    @Timed("endpoints.getTopReviewsByUser")
    public List<Review> getTopReviewsByUser(@PathVariable("userId") UUID userId) {
        return feedbackService.getTopReviewsByUser(userId);
    }

    @GetMapping("/reviews/user/{userId}/least")
    @Timed("endpoints.getLeastReviewsByUser")
    public List<Review> getLeastReviewsByUser(@PathVariable("userId") UUID userId) {
        return feedbackService.getLeastReviewsByUser(userId);
    }

    @GetMapping("/threads/root/user/{userId}")
    @Timed("endpoints.getMainThreadsByUser")
    public List<Thread> getMainThreadsByUser(@PathVariable("userId") UUID userId) {
        return feedbackService.getRootThreadsByUser(userId);
    }

    @GetMapping("/threads/sub/user/{userId}")
    @Timed("endpoints.getSubThreadsByUser")
    public List<Thread> getSubThreadsByUser(@PathVariable("userId") UUID userId) {
        return feedbackService.getSubThreadsByUser(userId);
    }

    @GetMapping("/complaints/vendor/{vendorId}")
    @Timed("endpoints.getComplaintsForVendor")
    public List<Complaint> getComplaintsForVendor(@PathVariable UUID vendorId) {
        return feedbackService.getComplaintsForVendor(vendorId);
    }

    @GetMapping("/complaints/user/{userId}")
    @Timed("endpoints.getComplaintsForUser")
    public List<Complaint> getComplaintsForUser(@PathVariable UUID userId) {
        return feedbackService.getComplaintsForUser(userId);
    }

    @GetMapping("/reviews/vendor/{vendorId}/top")
    @Timed("endpoints.getTopReviewsForVendor")
    public List<Review> getTopReviewsForVendor(@PathVariable("vendorId") UUID vendorId) {
        return feedbackService.getTopReviewsForVendor(vendorId);
    }

    @GetMapping("/reviews/vendor/{vendorId}/least")
    @Timed("endpoints.getLeastReviewsForVendor")
    public List<Review> getLeastReviewsForVendor(@PathVariable("vendorId") UUID vendorId) {
        return feedbackService.getLeastReviewsForVendor(vendorId);
    }

    @GetMapping("/{id}")
    @Timed("endpoints.getFeedbackById")
    public Feedback getFeedbackById(@PathVariable UUID id) {
        return feedbackService.getFeedbackById(id);
    }

    @PutMapping("/{id}")
    @Timed("endpoints.updateFeedback")
    public Feedback updateFeedback(
            @PathVariable UUID id,
            @RequestBody  Feedback feedback
    ) {
        feedback.setId(id);
        return feedbackService.updateFeedback(feedback);
    }

    @DeleteMapping("/{id}")
    @Timed("endpoints.deleteFeedback")
    public Feedback deleteFeedback(@PathVariable UUID id) {
        return feedbackService.deleteFeedbackById(id);
    }

    @PostMapping("/{id}/upvote")
    @Timed("endpoints.upvoteFeedback")
    public Feedback upvoteFeedback(@PathVariable UUID id, @RequestHeader("X-User-Id") UUID userId) {
        return feedbackService.upvoteFeedback(id,userId);
    }

    @PostMapping("/{id}/downvote")
    @Timed("endpoints.downvoteFeedback")
    public Feedback downvoteFeedback(@PathVariable UUID id, @RequestHeader("X-User-Id") UUID userId) {
        return feedbackService.downvoteFeedback(id,userId);
    }

    @PostMapping("/{id}/reply")
    @Timed("endpoints.replyToFeedback")
    public Feedback replyToFeedback(
            @PathVariable("id") UUID parentId,
            @RequestBody Feedback replyPayload
    ) {
        return feedbackService.replyToFeedback(parentId, replyPayload);
    }

}
