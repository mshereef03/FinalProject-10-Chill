package com.chill.feedback.factories;

import com.chill.feedback.models.Feedback;
import com.chill.feedback.models.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewFactory extends FeedbackFactory {
    @Override
    public Feedback createFeedback(Feedback feedback) {
        Review review = (Review) feedback;
        int rating = review.getRating();
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        return review;
    }
}
