package com.chill.user.controllers;

import com.chill.user.dto.*;
import com.chill.user.services.UserService;
import io.jsonwebtoken.JwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO request) {
        String token = userService.login(request.getUsername(), request.getPassword());
        return new LoginResponseDTO(token);
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
    public ResponseEntity<String> requestReset(@RequestBody PasswordResetRequestDTO request) {
        String resetToken = userService.requestPasswordReset(request.getEmail());
        return ResponseEntity.ok(resetToken);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetDTO request) {
        userService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Password reset successful");
    }

}


