package com.chill.feedback.factories;

import com.chill.feedback.models.Feedback;
import com.chill.feedback.models.Thread;
import org.springframework.stereotype.Component;

@Component
public class ThreadFactory extends FeedbackFactory {

    @Override
    public Feedback createFeedback(Feedback feedback) {
        return (Thread) feedback;
    }
}
