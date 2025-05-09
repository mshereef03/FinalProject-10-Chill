package com.chill.feedback;

import com.chill.feedback.factories.FeedbackFactory;
import com.chill.feedback.models.Feedback;
import com.chill.feedback.models.Review;
import com.chill.feedback.repositories.FeedbackRepository;
import com.chill.feedback.services.FeedbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackMicroServiceApplicationTest {

	@Mock
	private FeedbackRepository repo;

	@Mock
	private FeedbackFactory reviewFactory;

	private FeedbackService service;


	private Review sampleReview;

	@BeforeEach
	void setUp() {
		service = new FeedbackService(repo, List.of(reviewFactory));
		sampleReview = new Review();
		UUID id = UUID.randomUUID();
		sampleReview.setId(id);
		sampleReview.setComment("Great ride!");
		sampleReview.setRating(5);
	}

	@Test
	void createFeedback_success() {
		when(reviewFactory.supports(sampleReview)).thenReturn(true);
		when(reviewFactory.createFeedback(sampleReview)).thenReturn(sampleReview);
		when(repo.save(sampleReview)).thenReturn(sampleReview);

		Feedback result = service.createFeedback(sampleReview);

		assertThat(result).isSameAs(sampleReview);
		verify(repo).save(sampleReview);
	}

	@Test
	void createFeedback_unsupportedType() {
		when(reviewFactory.supports(sampleReview)).thenReturn(false);

		assertThatThrownBy(() -> service.createFeedback(sampleReview))
				.isInstanceOf(ResponseStatusException.class)
				.hasMessageContaining("Unsupported feedback type");
	}

	@Test
	void getFeedbackById_found() {
		when(repo.findById(sampleReview.getId()))
				.thenReturn(Optional.of(sampleReview));

		Feedback result = service.getFeedbackById(sampleReview.getId());

		assertThat(result).isSameAs(sampleReview);
	}

	@Test
	void getFeedbackById_notFound() {
		when(repo.findById(sampleReview.getId()))
				.thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.getFeedbackById(sampleReview.getId()))
				.isInstanceOf(ResponseStatusException.class)
				.hasMessageContaining("Feedback not found");
	}

	@Test
	void updateFeedback_changesCommentAndRating() {
		// prepare an updated instance with same ID
		Review updated = new Review();
		updated.setId(sampleReview.getId());
		updated.setComment("Updated comment");
		updated.setRating(4);

		when(repo.findById(sampleReview.getId()))
				.thenReturn(Optional.of(sampleReview));
		when(repo.save(sampleReview)).thenReturn(sampleReview);

		Feedback result = service.updateFeedback(updated);

		assertThat(sampleReview.getComment()).isEqualTo("Updated comment");
		assertThat(((Review)sampleReview).getRating()).isEqualTo(4);
		assertThat(result).isSameAs(sampleReview);
	}

	@Test
	void deleteFeedbackById_success() {
		when(repo.findById(sampleReview.getId()))
				.thenReturn(Optional.of(sampleReview));

		Feedback deleted = service.deleteFeedbackById(sampleReview.getId());

		verify(repo).delete(sampleReview);
		assertThat(deleted).isSameAs(sampleReview);
	}
}
