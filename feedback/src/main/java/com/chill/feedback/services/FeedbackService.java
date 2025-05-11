    package com.chill.feedback.services;
    
    import com.chill.feedback.command.FeedbackCommand;
    import com.chill.feedback.command.FeedbackCommandDispatcher;
    import com.chill.feedback.factories.FeedbackFactory;
    import com.chill.feedback.models.Feedback;
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

//        public void replyToFeedback(UUID feedbackId, Feedback reply) {
//            Feedback parent = getFeedbackById(feedbackId);
//            if (!(parent instanceof Thread)) {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Target is replayable: " + feedbackId);
//            }
//            Thread replyThread = (Thread) reply;
//            replyThread.setParentId(feedbackId);
//            feedbackRepository.save(replyThread);
//            ((Thread) parent).setAnswered(true);
//            feedbackRepository.save(parent);
//        }
    
//        public List<Feedback> sortFeedbacks(String strategy) {
//            List<Feedback> list = feedbackRepository.findAll();
//            if ("newest".equalsIgnoreCase(strategy)) {
//                list.sort(Comparator.comparing(Feedback::getCreatedAt).reversed());
//            } else if ("highest-rated".equalsIgnoreCase(strategy)) {
//                list.sort((a, b) -> {
//                    int ra = a instanceof Review ? ((Review) a).getRating() : 0;
//                    int rb = b instanceof Review ? ((Review) b).getRating() : 0;
//                    return Integer.compare(rb, ra);
//                });
//            }
//            return list;
//        }



    
        private Feedback findOrFail(UUID id) {
            return feedbackRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Feedback not found: " + id
                    ));
        }
    
    
    
    }
