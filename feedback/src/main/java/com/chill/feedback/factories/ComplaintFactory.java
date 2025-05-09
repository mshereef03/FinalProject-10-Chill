package com.chill.feedback.factories;

import com.chill.feedback.dtos.FeedbackDTO;
import com.chill.feedback.models.Complaint;
import com.chill.feedback.models.Feedback;
import com.chill.feedback.models.Review;

public class ComplaintFactory extends FeedbackFactory {

    public ComplaintFactory() {}

    @Override
    public Feedback createFeedback(FeedbackDTO feedbackDTO) {
        return new Complaint(feedbackDTO.getUserId(), feedbackDTO.getVendorId(), feedbackDTO.getOrderId(), feedbackDTO.getComment(), feedbackDTO.getTag());
    }

}
