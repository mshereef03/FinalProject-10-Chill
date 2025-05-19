package com.chill.feedback.controllers;

import com.chill.feedback.models.Feedback;
import com.chill.feedback.models.Review;
import com.chill.feedback.models.Thread;
import com.chill.feedback.models.Complaint;
import com.chill.feedback.services.FeedbackService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@ExtendWith(MockitoExtension.class)
class FeedbackControllerTest {

    @Mock
    private FeedbackService service;

    @InjectMocks
    private FeedbackController controller;

    private MockMvc mvc;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }
    @Test
    void GET_allFeedbacks_returnsList() throws Exception {
        Review r1 = new Review();
        r1.setId(UUID.randomUUID());
        r1.setComment("FB1");
        Review r2 = new Review();
        r2.setId(UUID.randomUUID());
        r2.setComment("FB2");

        given(service.getAllOfType(Feedback.class)).willReturn(List.of(r1, r2));

        mvc.perform(get("/feedbacks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].comment").value("FB1"))
                .andExpect(jsonPath("$[1].comment").value("FB2"));
    }

    @Test
    void GET_threads_returnsOnlyThreads() throws Exception {
        Thread t = new Thread();
        t.setId(UUID.randomUUID());
        t.setComment("Thread comment");

        given(service.getAllOfType(Thread.class)).willReturn(List.of(t));

        mvc.perform(get("/feedbacks/threads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].comment").value("Thread comment"));
    }

    @Test
    void GET_complaints_returnsOnlyComplaints() throws Exception {
        Complaint c = new Complaint();
        c.setId(UUID.randomUUID());
        c.setComment("Complaint comment");

        given(service.getAllOfType(Complaint.class)).willReturn(List.of(c));

        mvc.perform(get("/feedbacks/complaints"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].comment").value("Complaint comment"));
    }

    @Test
    void GET_feedbackById_found() throws Exception {
        UUID id = UUID.randomUUID();
        Review r = new Review();
        r.setId(id);
        r.setComment("Found");

        given(service.getFeedbackById(id)).willReturn(r);

        mvc.perform(get("/feedbacks/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.comment").value("Found"));
    }

    @Test
    void GET_feedbackById_notFound_returns404() throws Exception {
        UUID id = UUID.randomUUID();

        given(service.getFeedbackById(id))
                .willThrow(new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Feedback not found"
                ));

        mvc.perform(get("/feedbacks/{id}", id))
                .andExpect(status().isNotFound());
    }


    @Test
    void GET_complaintsForVendor_returnsList() throws Exception {
        UUID vendorId = UUID.randomUUID();

        Complaint c1 = new Complaint();
        c1.setId(UUID.randomUUID());
        c1.setVendorId(vendorId);
        c1.setComment("Complaint A");

        Complaint c2 = new Complaint();
        c2.setId(UUID.randomUUID());
        c2.setVendorId(vendorId);
        c2.setComment("Complaint B");

        given(service.getComplaintsForVendor(vendorId))
                .willReturn(List.of(c1, c2));

        mvc.perform(get("/feedbacks/complaints/vendor/{vendorId}", vendorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(c1.getId().toString()))
                .andExpect(jsonPath("$[0].comment").value("Complaint A"))
                .andExpect(jsonPath("$[1].comment").value("Complaint B"));
    }

    @Test
    void GET_complaintsForUser_returnsList() throws Exception {
        UUID userId = UUID.randomUUID();

        Complaint c = new Complaint();
        c.setId(UUID.randomUUID());
        c.setUserId(userId);
        c.setComment("My complaint");

        given(service.getComplaintsForUser(userId))
                .willReturn(List.of(c));

        mvc.perform(get("/feedbacks/complaints/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(c.getId().toString()))
                .andExpect(jsonPath("$[0].comment").value("My complaint"));
    }

    @Test
    void POST_createFeedback_returnsCreated() throws Exception {
        ObjectNode payload = mapper.createObjectNode()
                .put("type",    "review")
                .put("comment", "New feedback")
                .put("rating",  4);

        Review saved = new Review();
        saved.setId(UUID.randomUUID());
        saved.setComment("New feedback");
        saved.setRating(4);

        given(service.createFeedback(any(Review.class))).willReturn(saved);

        mvc.perform(post("/feedbacks")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("review"))
                .andExpect(jsonPath("$.id").value(saved.getId().toString()))
                .andExpect(jsonPath("$.comment").value("New feedback"))
                .andExpect(jsonPath("$.rating").value(4));
    }

    @Test
    void PUT_updateFeedback_appliesChanges() throws Exception {
        UUID id = UUID.randomUUID();
        ObjectNode payload = mapper.createObjectNode()
                .put("type",    "review")
                .put("comment", "Updated")
                .put("rating",  3);

        Review updated = new Review();
        updated.setId(id);
        updated.setComment("Updated");
        updated.setRating(3);

        given(service.updateFeedback(any(Review.class))).willReturn(updated);

        mvc.perform(put("/feedbacks/{id}", id)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("review"))
                .andExpect(jsonPath("$.comment").value("Updated"))
                .andExpect(jsonPath("$.rating").value(3));
    }


    @Test
    void DELETE_feedback_returnsDeleted() throws Exception {
        UUID id = UUID.randomUUID();
        Review r = new Review();
        r.setId(id);
        r.setComment("To delete");

        given(service.deleteFeedbackById(id)).willReturn(r);

        mvc.perform(delete("/feedbacks/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }



    @Test
    void POST_upvoteFeedback_returnsUpvoted() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Review upvoted = new Review();
        upvoted.setId(id);
        upvoted.setComment("Upvoted comment");
        upvoted.setRating(5);

        given(service.upvoteFeedback(id, userId)).willReturn(upvoted);

        mvc.perform(post("/feedbacks/{id}/upvote", id)
                        .header("X-User-Id", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.comment").value("Upvoted comment"))
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    void POST_downvoteFeedback_returnsDownvoted() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Review downvoted = new Review();
        downvoted.setId(id);
        downvoted.setComment("Downvoted comment");
        downvoted.setRating(4);

        given(service.downvoteFeedback(id, userId)).willReturn(downvoted);

        mvc.perform(post("/feedbacks/{id}/downvote", id)
                        .header("X-User-Id", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.comment").value("Downvoted comment"))
                .andExpect(jsonPath("$.rating").value(4));
    }

    @Test
    void POST_upvoteNonVotable_returnsBadRequest() throws Exception {
        UUID id     = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        // simulate that service refuses to upvote a Complaint
        given(service.upvoteFeedback(eq(id), eq(userId)))
                .willThrow(new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Cannot upvote type: Complaint"
                ));

        mvc.perform(post("/feedbacks/{id}/upvote", id)
                        .header("X-User-Id", userId.toString()))
                .andExpect(status().isBadRequest());
    }


    @Test
    void POST_replyFeedback_returnsReply() throws Exception {
        UUID parentId = UUID.randomUUID();

        ObjectNode payload = mapper.createObjectNode()
                .put("type",    "thread")
                .put("comment", "This is a reply");

        Thread savedReply = new Thread();
        savedReply.setId(UUID.randomUUID());
        savedReply.setComment("This is a reply");

        given(service.replyToFeedback(eq(parentId), any(Feedback.class)))
                .willReturn(savedReply);

        mvc.perform(post("/feedbacks/{id}/reply", parentId)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedReply.getId().toString()))
                .andExpect(jsonPath("$.comment").value("This is a reply"));
    }

    @Test
    void POST_replyNonReplyable_returnsBadRequest() throws Exception {
        UUID parentId = UUID.randomUUID();

        ObjectNode payload = mapper.createObjectNode()
                .put("type",    "review")        // a Review isn’t replyable
                .put("comment", "Won’t work");

        // stub service to throw 400
        given(service.replyToFeedback(eq(parentId), any(Feedback.class)))
                .willThrow(new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Cannot reply to feedback of type: Review"
                ));

        mvc.perform(post("/feedbacks/{id}/reply", parentId)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void GET_topReviewsForVendor_returnsList() throws Exception {
        UUID vendorId = UUID.randomUUID();

        Review r1 = new Review();
        r1.setId(UUID.randomUUID());
        r1.setVendorId(vendorId);
        r1.setRating(5);
        r1.setComment("Great!");

        Review r2 = new Review();
        r2.setId(UUID.randomUUID());
        r2.setVendorId(vendorId);
        r2.setRating(3);
        r2.setComment("Good");

        given(service.getTopReviewsForVendor(vendorId)).willReturn(List.of(r1, r2));

        mvc.perform(get("/feedbacks/reviews/vendor/{vendorId}/top", vendorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].rating").value(5))
                .andExpect(jsonPath("$[0].comment").value("Great!"))
                .andExpect(jsonPath("$[1].rating").value(3))
                .andExpect(jsonPath("$[1].comment").value("Good"));
    }

    @Test
    void GET_topReviewsForVendor_empty_returnsEmpty() throws Exception {
        UUID vendorId = UUID.randomUUID();

        given(service.getTopReviewsForVendor(vendorId))
                .willReturn(Collections.emptyList());

        mvc.perform(get("/feedbacks/reviews/vendor/{vendorId}/top", vendorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void GET_reviewsByUser_returnsList() throws Exception {
        UUID userId = UUID.randomUUID();
        Review r1 = new Review();
        r1.setId(UUID.randomUUID());
        r1.setUserId(userId);
        r1.setComment("User review A");
        r1.setRating(2);

        Review r2 = new Review();
        r2.setId(UUID.randomUUID());
        r2.setUserId(userId);
        r2.setComment("User review B");
        r2.setRating(4);

        given(service.getReviewsByUser(userId)).willReturn(List.of(r1, r2));

        mvc.perform(get("/feedbacks/reviews/user/{userId}/", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].comment").value("User review A"))
                .andExpect(jsonPath("$[0].rating").value(2))
                .andExpect(jsonPath("$[1].comment").value("User review B"))
                .andExpect(jsonPath("$[1].rating").value(4));

        verify(service).getReviewsByUser(userId);
    }

    @Test
    void GET_topReviewsByUser_returnsList() throws Exception {
        UUID userId = UUID.randomUUID();
        Review high = new Review();
        high.setId(UUID.randomUUID());
        high.setUserId(userId);
        high.setComment("Top review");
        high.setRating(5);

        Review low = new Review();
        low.setId(UUID.randomUUID());
        low.setUserId(userId);
        low.setComment("Lower review");
        low.setRating(1);

        given(service.getTopReviewsByUser(userId)).willReturn(List.of(high, low));

        mvc.perform(get("/feedbacks/reviews/user/{userId}/top", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].rating").value(5))
                .andExpect(jsonPath("$[0].comment").value("Top review"))
                .andExpect(jsonPath("$[1].rating").value(1))
                .andExpect(jsonPath("$[1].comment").value("Lower review"));

        verify(service).getTopReviewsByUser(userId);
    }

    @Test
    void GET_leastReviewsByUser_returnsList() throws Exception {
        UUID userId = UUID.randomUUID();
        Review low = new Review();
        low.setId(UUID.randomUUID());
        low.setUserId(userId);
        low.setComment("Worst review");
        low.setRating(1);

        Review high = new Review();
        high.setId(UUID.randomUUID());
        high.setUserId(userId);
        high.setComment("Better review");
        high.setRating(3);

        given(service.getLeastReviewsByUser(userId)).willReturn(List.of(low, high));

        mvc.perform(get("/feedbacks/reviews/user/{userId}/least", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].rating").value(1))
                .andExpect(jsonPath("$[0].comment").value("Worst review"))
                .andExpect(jsonPath("$[1].rating").value(3))
                .andExpect(jsonPath("$[1].comment").value("Better review"));

        verify(service).getLeastReviewsByUser(userId);
    }

    @Test
    void GET_mainThreadsByUser_returnsList() throws Exception {
        UUID userId = UUID.randomUUID();
        Thread t1 = new Thread();
        t1.setId(UUID.randomUUID());
        t1.setUserId(userId);
        t1.setComment("Root thread A");

        Thread t2 = new Thread();
        t2.setId(UUID.randomUUID());
        t2.setUserId(userId);
        t2.setComment("Root thread B");

        given(service.getRootThreadsByUser(userId)).willReturn(List.of(t1, t2));

        mvc.perform(get("/feedbacks/threads/root/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].comment").value("Root thread A"))
                .andExpect(jsonPath("$[1].comment").value("Root thread B"));

        verify(service).getRootThreadsByUser(userId);
    }

    @Test
    void GET_subThreadsByUser_returnsList() throws Exception {
        UUID userId = UUID.randomUUID();
        Thread reply = new Thread();
        reply.setId(UUID.randomUUID());
        reply.setUserId(userId);
        reply.setComment("Sub‐thread reply");

        given(service.getSubThreadsByUser(userId)).willReturn(List.of(reply));

        mvc.perform(get("/feedbacks/threads/sub/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].comment").value("Sub‐thread reply"));

        verify(service).getSubThreadsByUser(userId);
    }

    @Test
    void GET_leastReviewsForVendor_returnsList() throws Exception {
        UUID vendorId = UUID.randomUUID();
        Review r1 = new Review();
        r1.setId(UUID.randomUUID());
        r1.setVendorId(vendorId);
        r1.setComment("Worst vendor review");
        r1.setRating(1);

        Review r2 = new Review();
        r2.setId(UUID.randomUUID());
        r2.setVendorId(vendorId);
        r2.setComment("Better vendor review");
        r2.setRating(3);

        given(service.getLeastReviewsForVendor(vendorId)).willReturn(List.of(r1, r2));

        mvc.perform(get("/feedbacks/reviews/vendor/{vendorId}/least", vendorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].rating").value(1))
                .andExpect(jsonPath("$[0].comment").value("Worst vendor review"))
                .andExpect(jsonPath("$[1].rating").value(3))
                .andExpect(jsonPath("$[1].comment").value("Better vendor review"));

        verify(service).getLeastReviewsForVendor(vendorId);
    }
}
