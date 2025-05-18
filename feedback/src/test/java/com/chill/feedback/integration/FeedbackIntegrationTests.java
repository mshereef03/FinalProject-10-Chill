//package com.chill.feedback.integration;
//
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.springframework.http.*;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class FeedbackIntegrationTests {
//
//    private static final String BASE = "http://localhost:8082/feedbacks";
//    private final RestTemplate rt = new RestTemplate();
//
//    private static String reviewId;
//    private static String threadId;
//    private static String complaintId;
//
//    @Test @Order(1)
//    void createReview() {
//        Map<String,Object> payload = new HashMap<>();
//        payload.put("type",     "review");
//        payload.put("userId",   UUID.randomUUID().toString());
//        payload.put("vendorId", UUID.randomUUID().toString());
//        payload.put("orderId",  UUID.randomUUID().toString());
//        payload.put("comment",  "Great service");
//        payload.put("rating",   5);
//
//        HttpEntity<Map<String,Object>> req = new HttpEntity<>(payload, jsonHeaders());
//        ResponseEntity<Map> resp = rt.postForEntity(BASE, req, Map.class);
//
//        assertEquals(200, resp.getStatusCodeValue());
//        assertNotNull(resp.getBody().get("id"));
//        reviewId = resp.getBody().get("id").toString();
//    }
//
//    @Test @Order(2)
//    void createThread() {
//        Map<String,Object> payload = new HashMap<>();
//        payload.put("type",     "thread");
//        payload.put("userId",   UUID.randomUUID().toString());
//        payload.put("vendorId", UUID.randomUUID().toString());
//        payload.put("orderId",  UUID.randomUUID().toString());
//        payload.put("comment",  "When will my order ship?");
//        payload.put("parentId", null);
//
//        HttpEntity<Map<String,Object>> req = new HttpEntity<>(payload, jsonHeaders());
//        ResponseEntity<Map> resp = rt.postForEntity(BASE, req, Map.class);
//
//        assertEquals(200, resp.getStatusCodeValue());
//        assertEquals("When will my order ship?", resp.getBody().get("comment"));
//        threadId = resp.getBody().get("id").toString();
//    }
//
//    @Test @Order(3)
//    void createComplaint() {
//        Map<String,Object> payload = new HashMap<>();
//        payload.put("type",     "complaint");
//        payload.put("userId",   UUID.randomUUID().toString());
//        payload.put("vendorId", UUID.randomUUID().toString());
//        payload.put("orderId",  UUID.randomUUID().toString());
//        payload.put("comment",  "Package arrived damaged");
//        payload.put("tag",      "DAMAGED");
//
//        HttpEntity<Map<String,Object>> req = new HttpEntity<>(payload, jsonHeaders());
//        ResponseEntity<Map> resp = rt.postForEntity(BASE, req, Map.class);
//
//        assertEquals(200, resp.getStatusCodeValue());
//        assertEquals("DAMAGED", resp.getBody().get("tag"));
//        complaintId = resp.getBody().get("id").toString();
//    }
//
//    @Test @Order(4)
//    void getAllFeedbacks() {
//        ResponseEntity<List> resp = rt.getForEntity(BASE, List.class);
//        assertEquals(200, resp.getStatusCodeValue());
//        @SuppressWarnings("unchecked")
//        List<Map<String,Object>> all = resp.getBody();
//        List<String> ids = all.stream()
//                .map(m -> m.get("id").toString())
//                .toList();
//        assertTrue(ids.containsAll(List.of(reviewId, threadId, complaintId)));
//    }
//
//    @Test @Order(5)
//    void getReviews() {
//        ResponseEntity<List> resp = rt.getForEntity(BASE + "/reviews", List.class);
//        assertEquals(200, resp.getStatusCodeValue());
//
//        @SuppressWarnings("unchecked")
//        List<Map<String,Object>> reviews = resp.getBody();
//
//        List<String> ids = reviews.stream()
//                .map(m -> m.get("id").toString())
//                .toList();
//
//        assertTrue(ids.contains(reviewId),
//                "Expected reviewId " + reviewId + " to be returned among " + ids);
//    }
//
//    @Test @Order(6)
//    void getThreads() {
//        ResponseEntity<List> resp = rt.getForEntity(BASE + "/threads", List.class);
//        assertEquals(HttpStatus.OK, resp.getStatusCode());
//        @SuppressWarnings("unchecked")
//        List<Map<String,Object>> threads = resp.getBody();
//
//        List<String> ids = threads.stream()
//                .map(m -> m.get("id").toString())
//                .toList();
//        assertTrue(ids.contains(threadId),
//                "Expected threads to contain " + threadId + " but was " + ids);
//    }
//
//    @Test @Order(7)
//    void getComplaints() {
//        ResponseEntity<List> resp = rt.getForEntity(BASE + "/complaints", List.class);
//        assertEquals(HttpStatus.OK, resp.getStatusCode());
//
//        @SuppressWarnings("unchecked")
//        List<Map<String,Object>> complaints = resp.getBody();
//
//        List<String> ids = complaints.stream()
//                .map(m -> m.get("id").toString())
//                .toList();
//
//        assertTrue(ids.contains(complaintId),
//                "Expected complaint list to contain " + complaintId + " but was " + ids);
//    }
//
//    @Test @Order(8)
//    void getById() {
//        ResponseEntity<Map> resp = rt.getForEntity(BASE + "/" + reviewId, Map.class);
//        assertEquals(200, resp.getStatusCodeValue());
//        assertEquals(reviewId, resp.getBody().get("id").toString());
//    }
//
//    @Test @Order(9)
//    void updateReview() {
//        Map<String,Object> payload = new HashMap<>();
//        payload.put("type",    "review");
//        payload.put("comment", "Good, but could improve");
//        payload.put("rating",  4);
//
//        HttpEntity<Map<String,Object>> req = new HttpEntity<>(payload, jsonHeaders());
//        ResponseEntity<Map> resp = rt.exchange(
//                BASE + "/" + reviewId,
//                HttpMethod.PUT,
//                req,
//                Map.class
//        );
//        assertEquals(200, resp.getStatusCodeValue());
//        assertEquals(4, ((Number)resp.getBody().get("rating")).intValue());
//        assertEquals("Good, but could improve", resp.getBody().get("comment"));
//    }
//
//    @Test @Order(10)
//    void upvoteReview() {
//        UUID userId = UUID.randomUUID();
//        HttpHeaders headers = jsonHeaders();
//        headers.set("X-User-Id", userId.toString());
//        HttpEntity<Void> req = new HttpEntity<>(headers);
//        ResponseEntity<Map> resp = rt.exchange(
//                BASE + "/" + reviewId + "/upvote",
//                HttpMethod.POST,
//                req,
//                Map.class
//        );
//        assertEquals(HttpStatus.OK, resp.getStatusCode());
//        assertEquals(reviewId, resp.getBody().get("id").toString());
//    }
//
//    @Test @Order(11)
//    void downvoteReview() {
//        UUID userId = UUID.randomUUID();
//        HttpHeaders headers = jsonHeaders();
//        headers.set("X-User-Id", userId.toString());
//        HttpEntity<Void> req = new HttpEntity<>(headers);
//        ResponseEntity<Map> resp = rt.exchange(
//                BASE + "/" + reviewId + "/downvote",
//                HttpMethod.POST,
//                req,
//                Map.class
//        );
//        assertEquals(HttpStatus.OK, resp.getStatusCode());
//        assertEquals(reviewId, resp.getBody().get("id").toString());
//    }
//
//    @Test @Order(12)
//    void upvoteComplaintReturns400() {
//        UUID userId = UUID.randomUUID();
//        HttpHeaders headers = jsonHeaders();
//        headers.set("X-User-Id", userId.toString());
//        HttpEntity<Void> req = new HttpEntity<>(headers);
//
//        try {
//            rt.exchange(
//                    BASE + "/" + complaintId + "/upvote",
//                    HttpMethod.POST,
//                    req,
//                    String.class
//            );
//            fail("Expected 400 Bad Request when upvoting non-votable complaint");
//        } catch (HttpClientErrorException.BadRequest ex) {
//            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
//        }
//    }
//
//    @Test @Order(13)
//    void topReviewsForVendorIntegration() {
//        // pick a brand‐new vendorId so only these two reviews match
//        String vendorId = UUID.randomUUID().toString();
//
//        // create a low‐rated review
//        Map<String,Object> low = new HashMap<>();
//        low.put("type",     "review");
//        low.put("userId",   UUID.randomUUID().toString());
//        low.put("vendorId", vendorId);
//        low.put("orderId",  UUID.randomUUID().toString());
//        low.put("comment",  "Okay service");
//        low.put("rating",   2);
//        rt.postForEntity(BASE, new HttpEntity<>(low, jsonHeaders()), Map.class);
//
//        // create a high‐rated review
//        Map<String,Object> high = new HashMap<>();
//        high.put("type",     "review");
//        high.put("userId",   UUID.randomUUID().toString());
//        high.put("vendorId", vendorId);
//        high.put("orderId",  UUID.randomUUID().toString());
//        high.put("comment",  "Excellent!");
//        high.put("rating",   5);
//        rt.postForEntity(BASE, new HttpEntity<>(high, jsonHeaders()), Map.class);
//
//        // fetch top‐reviews
//        ResponseEntity<List> resp = rt.getForEntity(
//                BASE + "/reviews/vendor/" + vendorId + "/top",
//                List.class
//        );
//        assertEquals(200, resp.getStatusCodeValue());
//
//        @SuppressWarnings("unchecked")
//        List<Map<String,Object>> reviews = resp.getBody();
//        assertEquals(2, reviews.size());
//        // highest‐rated first
//        assertEquals(5, ((Number)reviews.get(0).get("rating")).intValue());
//        assertEquals("Excellent!", reviews.get(0).get("comment"));
//        assertEquals(2, ((Number)reviews.get(1).get("rating")).intValue());
//        assertEquals("Okay service", reviews.get(1).get("comment"));
//    }
//
//    @Test @Order(14)
//    void topReviewsForVendorEmptyIntegration() {
//        // a vendorId with no reviews
//        String vendorId = UUID.randomUUID().toString();
//
//        ResponseEntity<List> resp = rt.getForEntity(
//                BASE + "/reviews/vendor/" + vendorId + "/top",
//                List.class
//        );
//        assertEquals(200, resp.getStatusCodeValue());
//
//        @SuppressWarnings("unchecked")
//        List<Map<String,Object>> reviews = resp.getBody();
//        assertTrue(reviews.isEmpty());
//    }
//
//    @Test @Order(15)
//    void deleteReview() {
//        ResponseEntity<Map> resp = rt.exchange(
//                BASE + "/" + reviewId,
//                HttpMethod.DELETE,
//                null,
//                Map.class
//        );
//        assertEquals(200, resp.getStatusCodeValue());
//        assertEquals(reviewId, resp.getBody().get("id").toString());
//    }
//
//    @Test @Order(16)
//    void deletedReturns404() {
//        try {
//            rt.exchange(
//                    BASE + "/" + reviewId,
//                    HttpMethod.GET,
//                    null,
//                    String.class
//            );
//            fail("Expected 404 Not Found when retrieving deleted review");
//        } catch (HttpClientErrorException.NotFound ex) {
//            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
//        }
//    }
//
//    @Test @Order(17)
//    void replyToThread() {
//        // build a reply payload for a Thread
//        Map<String,Object> payload = new HashMap<>();
//        payload.put("type",     "thread");
//        payload.put("userId",   UUID.randomUUID().toString());
//        payload.put("vendorId", UUID.randomUUID().toString());
//        payload.put("orderId",  UUID.randomUUID().toString());
//        payload.put("comment",  "Thanks for the update!");
//
//        HttpEntity<Map<String,Object>> req = new HttpEntity<>(payload, jsonHeaders());
//        ResponseEntity<Map> resp = rt.postForEntity(
//                BASE + "/" + threadId + "/reply",
//                req,
//                Map.class
//        );
//
//        assertEquals(HttpStatus.OK, resp.getStatusCode());
//        Map<String,Object> body = resp.getBody();
//        assertNotNull(body.get("id"), "Expected reply to have its own id");
//        assertEquals(threadId, body.get("parentId").toString(),
//                "Expected parentId to be the original threadId");
//    }
//
//    @Test @Order(18)
//    void replyToNonReplyableReturns400() {
//        Map<String,Object> payload = new HashMap<>();
//        payload.put("type",     "thread");  // payload is a Thread, but parent is a Review
//        payload.put("userId",   UUID.randomUUID().toString());
//        payload.put("vendorId", UUID.randomUUID().toString());
//        payload.put("orderId",  UUID.randomUUID().toString());
//        payload.put("comment",  "This should fail");
//
//        HttpEntity<Map<String,Object>> req = new HttpEntity<>(payload, jsonHeaders());
//        try {
//            rt.postForEntity(
//                    BASE + "/" + complaintId + "/reply",
//                    req,
//                    String.class
//            );
//            fail("Expected 400 Bad Request when replying to a non-replyable feedback");
//        } catch (HttpClientErrorException.BadRequest ex) {
//            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
//        }
//    }
//
//    @Test @Order(19)
//    void getComplaintsForVendorIntegration() {
//        // pick a fresh vendorId
//        String vendorId = UUID.randomUUID().toString();
//
//        // create two complaints for that vendor
//        for (int i = 0; i < 2; i++) {
//            Map<String,Object> payload = new HashMap<>();
//            payload.put("type",     "complaint");
//            payload.put("userId",   UUID.randomUUID().toString());
//            payload.put("vendorId", vendorId);
//            payload.put("orderId",  UUID.randomUUID().toString());
//            payload.put("comment",  "Issue #" + i);
//            payload.put("tag",      "DAMAGED");
//
//            rt.postForEntity(BASE, new HttpEntity<>(payload, jsonHeaders()), Map.class);
//        }
//
//        // also create one complaint for a different vendor
//        {
//            Map<String,Object> payload = new HashMap<>();
//            payload.put("type",     "complaint");
//            payload.put("userId",   UUID.randomUUID().toString());
//            payload.put("vendorId", UUID.randomUUID().toString());
//            payload.put("orderId",  UUID.randomUUID().toString());
//            payload.put("comment",  "Other vendor");
//            payload.put("tag",      "DAMAGED");
//
//            rt.postForEntity(BASE, new HttpEntity<>(payload, jsonHeaders()), Map.class);
//        }
//
//        // fetch complaints for our vendor
//        ResponseEntity<List> resp = rt.getForEntity(
//                BASE + "/complaints/vendor/" + vendorId,
//                List.class
//        );
//        assertEquals(200, resp.getStatusCodeValue());
//
//        @SuppressWarnings("unchecked")
//        List<Map<String,Object>> complaints = resp.getBody();
//        assertEquals(2, complaints.size(), "Should only see complaints for that vendor");
//        for (Map<String,Object> c : complaints) {
//            assertEquals(vendorId, c.get("vendorId").toString());
//        }
//    }
//
//    @Test @Order(20)
//    void getComplaintsForUserIntegration() {
//        // pick a fresh userId
//        String userId = UUID.randomUUID().toString();
//
//        // create two complaints by that user
//        for (int i = 0; i < 2; i++) {
//            Map<String,Object> payload = new HashMap<>();
//            payload.put("type",     "complaint");
//            payload.put("userId",   userId);
//            payload.put("vendorId", UUID.randomUUID().toString());
//            payload.put("orderId",  UUID.randomUUID().toString());
//            payload.put("comment",  "User issue #" + i);
//            payload.put("tag",      "DAMAGED");
//
//            rt.postForEntity(BASE, new HttpEntity<>(payload, jsonHeaders()), Map.class);
//        }
//
//        {
//            Map<String,Object> payload = new HashMap<>();
//            payload.put("type",     "complaint");
//            payload.put("userId",   UUID.randomUUID().toString());
//            payload.put("vendorId", UUID.randomUUID().toString());
//            payload.put("orderId",  UUID.randomUUID().toString());
//            payload.put("comment",  "Other user");
//            payload.put("tag",      "DAMAGED");
//
//            rt.postForEntity(BASE, new HttpEntity<>(payload, jsonHeaders()), Map.class);
//        }
//
//        // fetch complaints for our user
//        ResponseEntity<List> resp = rt.getForEntity(
//                BASE + "/complaints/user/" + userId,
//                List.class
//        );
//        assertEquals(200, resp.getStatusCodeValue());
//
//        @SuppressWarnings("unchecked")
//        List<Map<String,Object>> complaints = resp.getBody();
//        assertEquals(2, complaints.size(), "Should only see complaints by that user");
//        for (Map<String,Object> c : complaints) {
//            assertEquals(userId, c.get("userId").toString());
//        }
//    }
//
//    private HttpHeaders jsonHeaders() {
//        HttpHeaders h = new HttpHeaders();
//        h.setContentType(MediaType.APPLICATION_JSON);
//        return h;
//    }
//}
