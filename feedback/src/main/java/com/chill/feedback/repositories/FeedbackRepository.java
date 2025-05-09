package com.chill.feedback.repositories;

import com.chill.feedback.models.*;
import com.chill.feedback.models.Thread;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public interface FeedbackRepository extends MongoRepository<Feedback, UUID> {
    List<Feedback> findByOrderId(UUID orderId);
    List<Feedback> findByVendorId(UUID vendorId);

    default <T extends Feedback> List<T> findByType(Class<T> type) {
        return findAll().stream()
                .filter(type::isInstance)
                .map(type::cast)
                .collect(Collectors.toList());
    }

    @Query("{ 'vendorId': ?0, '_class': 'com.chill.feedback.models.Review' }")
    List<Review> findReviewsByVendor(UUID vendorId);

    @Query("{ 'parentId': null, '_class': 'com.chill.feedback.models.ThreadFeedback' }")
    List<Thread> findRootThreads();

    List<Thread> findByParentId(UUID parentId);

    List<Complaint> findByTag(Tag tag);
}

