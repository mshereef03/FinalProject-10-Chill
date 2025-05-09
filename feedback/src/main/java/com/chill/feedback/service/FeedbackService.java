package com.chill.feedback.service;

import com.chill.feedback.model.Feedback;
import com.chill.feedback.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class FeedbackService {


    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }


    public Feedback createFeedback(Feedback feedback){
        return null;
    }

    public Feedback deleteFeedbackById(UUID feedbackId) {
        Feedback f = feedbackRepository.findById(feedbackId.toString())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Feedback not found: " + feedbackId));
        feedbackRepository.delete(f);
        return f;
    }

    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    public Feedback getFeedbackById(UUID feedbackId) {
        return feedbackRepository.findById(feedbackId.toString())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Feedback not found: " + feedbackId));
    }



//    public Feedback updateFeedback(Feedback feedback) {
//        Feedback existing = getFeedbackById(feedback.getFeedbackId());
//        existing.setComment(feedback.getComment());
//        if (existing instanceof Review && feedback instanceof Review) {
//            ((Review) existing).setRating(((Review) feedback).getRating());
//        }
//        return feedbackRepository.save(existing);
//    }

//    public void upvoteFeedback(UUID feedbackId) {
//        Feedback f = getFeedbackById(feedbackId);
//        f.upvote();
//        feedbackRepository.save(f);
//    }
//
//    public void downvoteFeedback(UUID feedbackId) {
//        Feedback f = getFeedbackById(feedbackId);
//        f.downvote();
//        feedbackRepository.save(f);
//    }

//    public void replyToFeedback(UUID feedbackId, Feedback reply) {
//        Feedback parent = getFeedbackById(feedbackId);
//        if (!(parent instanceof Thread)) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Target is replayable: " + feedbackId);
//        }
//        Thread replyThread = (Thread) reply;
//        replyThread.setParentId(feedbackId);
//        feedbackRepository.save(replyThread);
//        ((Thread) parent).setAnswered(true);
//        feedbackRepository.save(parent);
//    }

//    public List<Feedback> sortFeedbacks(String strategy) {
//        List<Feedback> list = feedbackRepository.findAll();
//        if ("newest".equalsIgnoreCase(strategy)) {
//            list.sort(Comparator.comparing(Feedback::getCreatedAt).reversed());
//        } else if ("highest-rated".equalsIgnoreCase(strategy)) {
//            list.sort((a, b) -> {
//                int ra = a instanceof Review ? ((Review) a).getRating() : 0;
//                int rb = b instanceof Review ? ((Review) b).getRating() : 0;
//                return Integer.compare(rb, ra);
//            });
//        }
//        return list;
//    }






}
