package com.chill.feedback.models;

import java.util.UUID;

public interface Votable {
    void upvote(UUID userId);
    void downvote(UUID userId);
    int getUpvoteCount();
    int getDownvoteCount();
}
