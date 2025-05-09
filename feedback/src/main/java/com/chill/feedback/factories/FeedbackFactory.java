package com.chill.feedback.factories;

import com.chill.feedback.models.Feedback;
import java.util.UUID;
import java.util.Date;

public abstract class FeedbackFactory {
    public abstract Feedback createFeedback(Feedback feedback);
    public abstract boolean supports(Feedback feedback);
}
