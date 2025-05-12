package com.chill.user.controllers;

import com.chill.user.dto.DecodedTokenDTO;
import com.chill.user.dto.LoginRequestDTO;
import com.chill.user.dto.LoginResponseDTO;
import com.chill.user.services.UserService;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
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
}


