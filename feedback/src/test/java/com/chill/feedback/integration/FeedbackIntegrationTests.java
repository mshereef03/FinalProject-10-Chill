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
//        @SuppressWarnings("unchecked")
//        List<Map<String,Object>> reviews = resp.getBody();
//        assertEquals(reviewId, reviews.get(0).get("id").toString());
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
//
//        assertEquals(200, resp.getStatusCodeValue());
//        assertEquals(4, ((Number)resp.getBody().get("rating")).intValue());
//        assertEquals("Good, but could improve", resp.getBody().get("comment"));
//    }
//
//    @Test @Order(10)
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
//    @Test @Order(11)
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
//    private HttpHeaders jsonHeaders() {
//        HttpHeaders h = new HttpHeaders();
//        h.setContentType(MediaType.APPLICATION_JSON);
//        return h;
//    }
//}
