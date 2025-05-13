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

    default <T extends Feedback> List<T> findByType(Class<T> type) {
        return findAll().stream()
                .filter(type::isInstance)
                .map(type::cast)
                .collect(Collectors.toList());
    }

    @Query(
        value = "{ 'userId': ?0, '_class': 'com.chill.feedback.models.Complaint' }"
    )
    List<Complaint> findComplaintsByUserId(UUID userId);

    @Query(
        value = "{ 'vendorId': ?0, '_class': 'com.chill.feedback.models.Complaint' }"
    )
    List<Complaint> findComplaintsByVendorId(UUID vendorId);

    @Query(
        value = "{ 'vendorId': ?0, '_class': 'com.chill.feedback.models.Review' }",
        sort  = "{ 'rating': -1 }"
    )
    List<Review> findByVendorIdOrderByRatingDesc(UUID vendorId);

    @Query(
        value = "{ 'vendorId': ?0, '_class': 'com.chill.feedback.models.Review' }",
        sort  = "{ 'rating': 1 }"
    )
    List<Review> findByVendorIdOrderByRatingAsc(UUID vendorId);

    @Query(
        value = "{ 'userId' :  ?0, '_class':  'com.chill.feedback.models.Review'}"
    )
    List<Review> findReviewsByUserId(UUID userId);

    @Query(
        value = "{ 'userId' :  ?0, '_class':  'com.chill.feedback.models.Review'}",
        sort  = "{ 'rating': -1 }"
    )
    List<Review> findReviewsByUserIdOrderByRatingDesc(UUID userId);

    @Query(
        value = "{ 'userId' :  ?0, '_class':  'com.chill.feedback.models.Review'}",
        sort  = "{ 'rating': 1 }"
    )
    List<Review> findReviewsByUserIdOrderByRatingAsc(UUID userId);

    @Query(
        value = "{ 'userId' :  ?0, '_class':  'com.chill.feedback.models.Thread'}"
    )
    List<Thread> findThreadsByUserId(UUID userId);

    @Query(
        value = "{ 'userId': ?0, '_class': 'com.chill.feedback.models.Thread', 'parentId': null }"
    )
    List<Thread> findRootThreadsByUserId(UUID userId);

    @Query(
        value = "{ 'userId': ?0, '_class': 'com.chill.feedback.models.Thread', 'parentId': { $ne: null } }"
    )
    List<Thread> findSubThreadsByUserId(UUID userId);

    @Query(
        value = "{ '_class': 'com.chill.feedback.models.Thread', 'parentId': ?0 }"
    )
    List<Thread> findSubThreadsByParentId(UUID id);
}