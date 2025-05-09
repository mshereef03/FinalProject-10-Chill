package com.chill.feedback.factories;

import com.chill.feedback.dtos.FeedbackDTO;
import com.chill.feedback.models.Feedback;

public abstract class FeedbackFactory {

    public abstract Feedback createFeedback(FeedbackDTO feedbackDTO);

}
