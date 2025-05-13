package com.chill.user.controllers;

import com.chill.user.dto.DecodedTokenDTO;
import com.chill.user.dto.LoginRequestDTO;
import com.chill.user.dto.LoginResponseDTO;
import com.chill.user.models.UserModel;
import com.chill.user.services.UserService;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // Create
    @PostMapping
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
}


