package com.chill.user.controller;

import com.chill.user.dto.*;
import com.chill.user.model.UserModel;
import com.chill.user.messaging.RabbitMQProducer;
import com.chill.user.service.UserService;
import io.jsonwebtoken.JwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {


    private final UserService userService;
    private final RabbitMQProducer producer;

    public UserController(UserService userService, RabbitMQProducer producer) {
        this.userService = userService;
        this.producer = producer;
    }

    // Create
    @PostMapping("/create")
    public UserModel createUser(@RequestBody UserModel user) {
        return userService.createUser(user);
    }

    // Read all
    @GetMapping
    public List<UserModel> getAllUsers() {
        return userService.getAllUsers();
    }

    // Read one
    @GetMapping("/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/{id}")
    public UserModel updateUser(@PathVariable Long id, @RequestBody UserModel user) {
        return userService.updateUser(id, user);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearDB() {
        userService.clearDatabase();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public TokenResponseDTO login(@RequestBody LoginRequestDTO request) {
        String token = userService.login(request);
        return new TokenResponseDTO(token);
    }

    @GetMapping("/verify-token")
    public DecodedTokenDTO verifyToken(
            @RequestHeader(name = "Authorization", required = true) String authHeader) {

        if (!authHeader.startsWith("Bearer ")) {
            throw new JwtException("Missing Bearer token");
        }
        String token = authHeader.substring(7);
        return userService.decodeToken(token);
    }

    @PostMapping("/request-reset")
    public TokenResponseDTO requestReset(@RequestBody PasswordResetRequestDTO request) {
        String resetToken = userService.requestPasswordReset(request.getEmail());
        return new TokenResponseDTO(resetToken);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetDTO request) {
        userService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Password reset successful");
    }


    @PostMapping("/request-email-verification")
    public TokenResponseDTO requestEmailVerification(
            @RequestHeader(name = "Authorization", required = true) String authHeader) {
        if (!authHeader.startsWith("Bearer ")) {
            throw new JwtException("Missing Bearer token");
        }
        String loginToken = authHeader.substring(7);
        String verificationToken = userService.requestEmailVerification(loginToken);
        return new TokenResponseDTO(verificationToken);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> resetPassword(@RequestBody VerifyEmailDTO request) {
        userService.verifyEmail(request.getToken());
        return ResponseEntity.ok("Email verified successfully");
    }

    @PostMapping("/submit-feedback")
    public ResponseEntity<Void> submitFeedback(@RequestBody FeedbackDTO feedback) {
        producer.sendFeedback(feedback);
        return ResponseEntity.accepted().build();
    }
}


