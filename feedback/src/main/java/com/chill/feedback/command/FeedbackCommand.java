package com.chill.feedback.command;

import com.chill.feedback.models.Feedback;

import java.util.UUID;

public interface FeedbackCommand {

    String getName();

    Feedback execute(UUID feedbackId, UUID userId);
}
