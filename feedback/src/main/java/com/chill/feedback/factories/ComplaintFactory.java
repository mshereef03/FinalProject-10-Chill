package com.chill.feedback.factories;

import com.chill.feedback.models.Feedback;
import com.chill.feedback.models.Complaint;
import org.springframework.stereotype.Component;

@Component
public class ComplaintFactory extends FeedbackFactory {
    @Override
    public Feedback createFeedback(Feedback feedback) {
        return (Complaint) feedback;
    }
}