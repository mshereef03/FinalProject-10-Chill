package com.chill.feedback.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import com.chill.feedback.models.Feedback;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class FeedbackCommandDispatcher {

    private final Map<String, FeedbackCommand> commandMap;

    @Autowired
    public FeedbackCommandDispatcher(List<FeedbackCommand> commands) {
        this.commandMap = commands.stream()
                .collect(Collectors.toMap(
                        c -> c.getName().toLowerCase(),
                        c -> c
                ));
    }

    public Feedback dispatch(String commandName, UUID feedbackId, UUID userId) {
        FeedbackCommand cmd = commandMap.get(commandName.toLowerCase());
        if (cmd == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unknown command: " + commandName
            );
        }
        return cmd.execute(feedbackId, userId);
    }
}
