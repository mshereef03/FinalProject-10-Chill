package com.chill.feedback.factories;

import com.chill.feedback.models.Feedback;
import com.chill.feedback.models.Complaint;
import org.springframework.stereotype.Component;

@Component
public class ComplaintFactory extends FeedbackFactory {

    @Override
    public boolean supports(Feedback feedback) {
        return feedback instanceof Complaint;
    }

    @Override
    public Feedback createFeedback(Feedback feedback) {
        Complaint c = (Complaint) feedback;
//        if (c.getTag() == null) {
//            throw new IllegalArgumentException("Complaint must have a tag");
//        }
        return c;
    }
}
