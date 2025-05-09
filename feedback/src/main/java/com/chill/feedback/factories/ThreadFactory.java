package com.chill.feedback.factories;

import com.chill.feedback.dtos.FeedbackDTO;
import com.chill.feedback.models.Feedback;
import com.chill.feedback.models.Thread;

public class ThreadFactory extends FeedbackFactory {

    public ThreadFactory() {}

    @Override
    public Feedback createFeedback(FeedbackDTO feedbackDTO) {
        return new Thread(feedbackDTO.getUserId(), feedbackDTO.getVendorId(), feedbackDTO.getOrderId(), feedbackDTO.getComment(), feedbackDTO.getParentId());
    }
}
