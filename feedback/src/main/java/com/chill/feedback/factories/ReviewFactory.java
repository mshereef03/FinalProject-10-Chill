package com.chill.feedback.factories;

import com.chill.feedback.dtos.FeedbackDTO;
import com.chill.feedback.models.Feedback;
import com.chill.feedback.models.Review;

public class ReviewFactory extends FeedbackFactory{

    public ReviewFactory() {}

    @Override
    public Feedback createFeedback(FeedbackDTO feedbackDTO) {
        return new Review(feedbackDTO.getUserId(), feedbackDTO.getVendorId(), feedbackDTO.getOrderId(), feedbackDTO.getComment(), feedbackDTO.getRating());
    }

}
