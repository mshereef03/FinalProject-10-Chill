    package com.chill.feedback.services;
    
    import com.chill.feedback.command.FeedbackCommand;
    import com.chill.feedback.command.FeedbackCommandDispatcher;
    import com.chill.feedback.factories.FeedbackFactory;
    import com.chill.feedback.models.Complaint;
    import com.chill.feedback.models.Feedback;
    import com.chill.feedback.models.Replyable;
    import com.chill.feedback.models.Review;
    import com.chill.feedback.repositories.FeedbackRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.stereotype.Service;
    import org.springframework.web.server.ResponseStatusException;
    
    import java.util.List;
    import java.util.Map;
    import java.util.UUID;
    import java.util.stream.Collectors;

    @Service
    public class FeedbackService {
    
    
        private final FeedbackRepository feedbackRepository;
        private final List<FeedbackFactory> feedbackFactories;
        private final FeedbackCommandDispatcher dispatcher;
        @Autowired
        public FeedbackService(FeedbackRepository feedbackRepository,
                               List<FeedbackFactory> feedbackFactories,
                               FeedbackCommandDispatcher dispatcher) {
            this.feedbackRepository       = feedbackRepository;
            this.feedbackFactories  = feedbackFactories;
            this.dispatcher = dispatcher;
        }
    
    
        public Feedback createFeedback(Feedback feedback) {
            FeedbackFactory factory = feedbackFactories.stream()
                    .filter(f -> f.supports(feedback))
                    .findFirst()
                    .orElseThrow(() ->
                            new ResponseStatusException(
                                    HttpStatus.BAD_REQUEST,
                                    "Unsupported feedback type: " + feedback.getClass().getSimpleName()
                            )
                    );
            Feedback f = factory.createFeedback(feedback);
            return feedbackRepository.save(f);
        }
    
    
        public Feedback getFeedbackById(UUID feedbackId) {
            return findOrFail(feedbackId);
        }

    
        public <T extends Feedback> List<T> getAllOfType(Class<T> type) {
            if (!Feedback.class.isAssignableFrom(type)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid feedback type: " + type.getSimpleName());
            }
            return feedbackRepository.findByType(type);
        }
    
    
        public Feedback updateFeedback(Feedback feedback) {
            Feedback existing = findOrFail(feedback.getId());
            existing.setComment(feedback.getComment());
            if (existing instanceof Review && feedback instanceof Review) {
                ((Review) existing).setRating(((Review) feedback).getRating());
            }
            return feedbackRepository.save(existing);
        }
    
    
        public Feedback deleteFeedbackById(UUID feedbackId) {
            Feedback deleted = findOrFail(feedbackId);
            feedbackRepository.delete(deleted);
            return deleted;
        }
    
        public Feedback upvoteFeedback(UUID feedbackId, UUID userId) {
            return dispatcher.dispatch("upvote", feedbackId, userId);
        }
    
        public Feedback downvoteFeedback(UUID feedbackId, UUID userId) {
            return dispatcher.dispatch("downvote", feedbackId, userId);
        }


        public Feedback replyToFeedback(UUID parentId, Feedback replyPayload) {
            Feedback parent = findOrFail(parentId);
            if (!(parent instanceof Replyable)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Cannot reply to feedback of type: " + parent.getClass().getSimpleName()
                );
            }

            if (!(replyPayload instanceof Replyable)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Reply payload must implement Replyable"
                );
            }

            ((Replyable) replyPayload).setParentId(parentId);
            return feedbackRepository.save(replyPayload);
        }

        public List<Review> getTopReviewsForVendor(UUID vendorId) {
            return feedbackRepository.findByVendorIdOrderByRatingDesc(vendorId);
        }


        public List<Complaint> getComplaintsForVendor(UUID vendorId) {
            return feedbackRepository.findComplaintsByVendorId(vendorId);
        }


        public List<Complaint> getComplaintsForUser(UUID userId) {
            return feedbackRepository.findComplaintsByUserId(userId);
        }



    
        private Feedback findOrFail(UUID id) {
            return feedbackRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Feedback not found: " + id
                    ));
        }




    
    
    
    }
