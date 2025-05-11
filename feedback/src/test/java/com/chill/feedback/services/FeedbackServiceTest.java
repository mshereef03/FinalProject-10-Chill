//package com.chill.feedback.services;
//
//import com.chill.feedback.command.FeedbackCommand;
//import com.chill.feedback.command.FeedbackCommandDispatcher;
//import com.chill.feedback.factories.FeedbackFactory;
//import com.chill.feedback.models.Feedback;
//import com.chill.feedback.models.Review;
//import com.chill.feedback.repositories.FeedbackRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.*;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class FeedbackServiceTest {
//
//    @Mock
//    private FeedbackRepository repo;
//
//    @Mock
//    private FeedbackFactory reviewFactory;
//
//    @Mock
//    private FeedbackCommand upvoteCmd;
//
//    @Mock
//    private FeedbackCommand downvoteCmd;
//
//    private FeedbackService service;
//
//    private Review sampleReview;
//    private final UUID feedbackId = UUID.randomUUID();
//    private final UUID userId     = UUID.randomUUID();
//    private final Feedback fakeFeedback = new Feedback() {{ setId(feedbackId); }};
//
//    @BeforeEach
//    void setUp() {
//		Map<String,FeedbackCommand> feedbackCommandMap = Map.of(
//				"upvote",   upvoteCmd,
//				"downvote", downvoteCmd
//		);
//		service = new FeedbackService(repo, List.of(reviewFactory), new FeedbackCommandDispatcher(feedbackCommandMap));
//		sampleReview = new Review();
//        sampleReview.setId(feedbackId);
//        sampleReview.setComment("Great ride!");
//        sampleReview.setRating(5);
//    }
//
//	@Test
//	void createFeedback_success() {
//		when(reviewFactory.supports(sampleReview)).thenReturn(true);
//		when(reviewFactory.createFeedback(sampleReview)).thenReturn(sampleReview);
//		when(repo.save(sampleReview)).thenReturn(sampleReview);
//
//		Feedback result = service.createFeedback(sampleReview);
//
//		assertThat(result).isSameAs(sampleReview);
//		verify(repo).save(sampleReview);
//	}
//
//	@Test
//	void createFeedback_unsupportedType() {
//		when(reviewFactory.supports(sampleReview)).thenReturn(false);
//
//		assertThatThrownBy(() -> service.createFeedback(sampleReview))
//				.isInstanceOf(ResponseStatusException.class)
//				.hasMessageContaining("Unsupported feedback type");
//	}
//
//	@Test
//	void getFeedbackById_found() {
//		when(repo.findById(sampleReview.getId()))
//				.thenReturn(Optional.of(sampleReview));
//
//		Feedback result = service.getFeedbackById(sampleReview.getId());
//
//		assertThat(result).isSameAs(sampleReview);
//	}
//
//
//
//
//	@Test
//	void getAllOfType_returnsOnlyThatSubtype() {
//		// ─── Arrange ──────────────────────────────────────────────────────────────
//		Review r1 = new Review(); r1.setId(UUID.randomUUID()); r1.setComment("r1");r1.setRating(5);
//		Review r2 = new Review(); r2.setId(UUID.randomUUID()); r2.setComment("r2");r2.setRating(4);
//
//		// tell the mocked repository exactly what to return for this call
//		when(repo.findByType(Review.class)).thenReturn(List.of(r1, r2));
//
//		List<Review> result = service.getAllOfType(Review.class);
//
//		assertThat(result)
//				.hasSize(2)
//				.containsExactly(r1, r2);
//
//		verify(repo).findByType(Review.class);
//	}
//
//	@Test
//	@SuppressWarnings("unchecked")
//	void getAllOfType_nonFeedbackClass_throwsBadRequest() {
//		Class invalid = String.class;
//		assertThatThrownBy(() -> service.getAllOfType(invalid))
//				.isInstanceOf(ResponseStatusException.class)
//				.hasMessageContaining("Invalid feedback type");
//		verify(repo, never()).findByType(any());
//	}
//
//
//	@Test
//	void getFeedbackById_notFound()
//	{
//		when(repo.findById(sampleReview.getId()))
//				.thenReturn(Optional.empty());
//
//		assertThatThrownBy(() -> service.getFeedbackById(sampleReview.getId()))
//				.isInstanceOf(ResponseStatusException.class)
//				.hasMessageContaining("Feedback not found");
//	}
//
//	@Test
//	void updateFeedback_changesCommentAndRating() {
//		Review updated = new Review();
//		updated.setId(sampleReview.getId());
//		updated.setComment("Updated comment");
//		updated.setRating(4);
//
//		when(repo.findById(sampleReview.getId()))
//				.thenReturn(Optional.of(sampleReview));
//		when(repo.save(sampleReview)).thenReturn(sampleReview);
//
//		Feedback result = service.updateFeedback(updated);
//
//		assertThat(sampleReview.getComment()).isEqualTo("Updated comment");
//		assertThat(((Review)sampleReview).getRating()).isEqualTo(4);
//		assertThat(result).isSameAs(sampleReview);
//	}
//
//
//	@Test
//	void updateFeedback_notFound()
//	{
//		UUID id = UUID.randomUUID();
//		Review incoming = new Review();
//		incoming.setId(id);
//		incoming.setComment("Should fail");
//		incoming.setRating(3);
//		when(repo.findById(id)).thenReturn(Optional.empty());   // DB says “not found”
//		assertThatThrownBy(() -> service.updateFeedback(incoming))
//				.isInstanceOf(ResponseStatusException.class)
//				.hasMessageContaining("Feedback not found");
//		verify(repo, never()).save(any());
//	}
//
//	@Test
//	void deleteFeedbackById_success() {
//		when(repo.findById(sampleReview.getId()))
//				.thenReturn(Optional.of(sampleReview));
//
//		Feedback deleted = service.deleteFeedbackById(sampleReview.getId());
//
//		verify(repo).delete(sampleReview);
//		assertThat(deleted).isSameAs(sampleReview);
//	}
//
//	@Test
//	void deleteFeedbackById_notFound() {
//		UUID id = UUID.randomUUID();
//
//		when(repo.findById(id)).thenReturn(Optional.empty());
//
//		assertThatThrownBy(() -> service.deleteFeedbackById(id))
//				.isInstanceOf(ResponseStatusException.class)
//				.hasMessageContaining("Feedback not found");
//
//		verify(repo, never()).delete(any());
//	}
//
//
//    @Test
//    void upvoteFeedback_delegatesToUpvoteCommand() {
//        when(upvoteCmd.execute(feedbackId, userId)).thenReturn(fakeFeedback);
//        Feedback result = service.upvoteFeedback(feedbackId, userId);
//        assertThat(result).isSameAs(fakeFeedback);
//        verify(upvoteCmd).execute(feedbackId, userId);
//        verifyNoInteractions(downvoteCmd);
//    }
//
//    @Test
//    void downvoteFeedback_delegatesToDownvoteCommand() {
//        when(downvoteCmd.execute(feedbackId, userId)).thenReturn(fakeFeedback);
//
//        Feedback result = service.downvoteFeedback(feedbackId, userId);
//
//        assertThat(result).isSameAs(fakeFeedback);
//        verify(downvoteCmd).execute(feedbackId, userId);
//        verifyNoInteractions(upvoteCmd);
//    }
//
//    @Test
//    void unknownCommand_throwsBadRequest() {
//        FeedbackService emptySvc = new FeedbackService(repo, List.of(reviewFactory), new FeedbackCommandDispatcher(Map.of()));
//        assertThatThrownBy(() -> emptySvc.upvoteFeedback(feedbackId, userId))
//                .isInstanceOf(ResponseStatusException.class)
//                .hasMessageContaining("Unknown command");
//    }
//
//
//}
