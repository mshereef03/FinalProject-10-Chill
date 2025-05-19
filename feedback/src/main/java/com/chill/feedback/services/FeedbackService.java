    package com.chill.feedback.services;
    
    import com.chill.feedback.command.FeedbackCommandDispatcher;
    import com.chill.feedback.factories.ComplaintFactory;
    import com.chill.feedback.factories.FeedbackFactory;
    import com.chill.feedback.factories.ReviewFactory;
    import com.chill.feedback.factories.ThreadFactory;
    import com.chill.feedback.models.Complaint;
    import com.chill.feedback.models.Feedback;
    import com.chill.feedback.models.Replyable;
    import com.chill.feedback.models.Review;
    import com.chill.feedback.rabbitmq.RabbitMQConfig;
    import com.chill.feedback.repositories.FeedbackRepository;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.amqp.rabbit.annotation.RabbitListener;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.stereotype.Service;
    import org.springframework.web.server.ResponseStatusException;
    
    import java.util.List;
    import java.util.UUID;

    import com.chill.feedback.models.Feedback;
    import com.chill.feedback.models.Review;
    import com.chill.feedback.models.Thread;
    import com.chill.feedback.models.Complaint;

    @Slf4j
    @Service
    public class FeedbackService {
    
        private final FeedbackRepository feedbackRepository;
        private final FeedbackCommandDispatcher dispatcher;

        @Autowired
        public FeedbackService(FeedbackRepository feedbackRepository, FeedbackCommandDispatcher dispatcher) {
            this.feedbackRepository = feedbackRepository;
            this.dispatcher = dispatcher;
        }
    
        public static FeedbackFactory initialize(Feedback feedback) throws Exception {
            if (feedback instanceof Thread) {
                return new ThreadFactory();
            } else if (feedback instanceof Complaint) {
                return new ComplaintFactory();
            } else if (feedback instanceof Review) {
                return new ReviewFactory();
            } else {
                throw new Exception("Error! Unknown feedback type!");
            }
        }


        public Feedback createFeedback(Feedback feedback) {
            try {
                FeedbackFactory feedbackFactory = initialize(feedback);
                Feedback f = feedbackFactory.createFeedback(feedback);
                return feedbackRepository.save(f);
            } catch (Exception e) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        feedback.getClass().getSimpleName() + " can not be created!"
                );
            }
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

        private void deleteThreadRecursively(Thread thread) {
            List<Thread> children = feedbackRepository.findSubThreadsByParentId(thread.getId());

            for (Thread child : children) {
                deleteThreadRecursively(child);
            }

            feedbackRepository.delete(thread);
        }

        public Feedback deleteFeedbackById(UUID feedbackId) {
            Feedback deleted = findOrFail(feedbackId);

            if (deleted instanceof Thread) {
                deleteThreadRecursively((Thread) deleted);
            } else {
                feedbackRepository.delete(deleted);
            }

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

        public List<Review> getLeastReviewsForVendor(UUID vendorId) {
            return feedbackRepository.findByVendorIdOrderByRatingAsc(vendorId);
        }

        public List<Complaint> getComplaintsForVendor(UUID vendorId) {
            return feedbackRepository.findComplaintsByVendorId(vendorId);
        }

        public List<Complaint> getComplaintsForUser(UUID userId) {
            return feedbackRepository.findComplaintsByUserId(userId);
        }

        public List<Review> getReviewsByUser(UUID userId) {
            return feedbackRepository.findReviewsByUserId(userId);
        }

        public List<Review> getTopReviewsByUser(UUID userId) {
            return feedbackRepository.findReviewsByUserIdOrderByRatingDesc(userId);
        }

        public List<Review> getLeastReviewsByUser(UUID userId) {
            return feedbackRepository.findReviewsByUserIdOrderByRatingAsc(userId);
        }

        public List<Thread> getRootThreadsByUser(UUID userId) {
            return feedbackRepository.findRootThreadsByUserId(userId);
        }

        public List<Thread> getSubThreadsByUser(UUID userId) {
            return feedbackRepository.findSubThreadsByUserId(userId);
        }
    
        private Feedback findOrFail(UUID id) {
            return feedbackRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Feedback not found: " + id
                    ));
        }

    }
