package com.chill.feedback.factories;

import com.chill.feedback.models.Feedback;
import com.chill.feedback.models.Thread;
import org.springframework.stereotype.Component;

@Component
public class ThreadFactory extends FeedbackFactory {

    @Override
    public boolean supports(Feedback feedback) {
        return feedback instanceof Thread;
    }

    @Override
    public Feedback createFeedback(Feedback feedback) {
        Thread thread = (Thread) feedback;
        if (thread.getParentId() == null) {
            // top-level question: fine

        }
        return thread;
    }
}
