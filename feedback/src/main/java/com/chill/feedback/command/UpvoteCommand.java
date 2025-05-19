package com.chill.feedback.command;

import com.chill.feedback.command.FeedbackCommand;
import com.chill.feedback.models.Feedback;
import com.chill.feedback.models.Votable;
import com.chill.feedback.repositories.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Component
public class UpvoteCommand implements FeedbackCommand {
    private final FeedbackRepository feedbackRepository;

    @Autowired
    public UpvoteCommand(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public String getName() {
        return "upvote";
    }

    @Override
    public Feedback execute(UUID id, UUID userId) {
        Feedback f = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Feedback not found: " + id));
        if (!(f instanceof Votable)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cannot upvote type: " + f.getClass().getSimpleName());
        }
        ((Votable)f).upvote(userId);
        return feedbackRepository.save(f);
    }
}
