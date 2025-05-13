package com.chill.user.services;

import com.chill.user.JwtUtil;
import com.chill.user.dto.DecodedTokenDTO;
import com.chill.user.exceptions.InvalidCredentialsException;
import com.chill.user.models.UserModel;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final JwtUtil jwtUtil;
    // temporary simulated user store until db setup
    private final Map<String, UserModel> users = new HashMap<>();

    public UserService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;

        // Simulated user DB entries
        users.put("testuser@example.com", new UserModel(
                1L,
                "testuser",                      // username
                "test1234",                      // password
                "testuser@example.com",          // email
                List.of("ROLE_USER")             // roles
        ));

        users.put("admin@example.com", new UserModel(
                2L,
                "admin",                         // username
                "adminpass",                     // password
                "admin@example.com",             // email
                List.of("ROLE_ADMIN", "ROLE_USER")
        ));
    }




    public String login(String username, String password) {
        // Dummy hardcoded users for now
        if ("admin".equals(username) && "adminpass".equals(password)) {
            UserModel admin = new UserModel();
            admin.setId(1L);
            admin.setUsername("admin");
            admin.setPassword("adminpass");
            admin.setRoles(List.of("ROLE_ADMIN", "ROLE_USER"));
            return jwtUtil.generateToken(admin);
        }

        if ("user".equals(username) && "userpass".equals(password)) {
            UserModel user = new UserModel();
            user.setId(2L);
            user.setUsername("user");
            user.setPassword("userpass");
            user.setRoles(List.of("ROLE_USER"));
            return jwtUtil.generateToken(user);
        }

        throw new InvalidCredentialsException("Invalid Credentials !");
    }

    public DecodedTokenDTO decodeToken(String token){
        return jwtUtil.decodeToken(token);
    }


    public String requestPasswordReset(String email) {
        // Simulated lookup until db setup
        System.out.println("email: " + email);
        UserModel user = users.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User with email not found"));

        return jwtUtil.generateResetToken(user); // This token will expire in e.g., 10 minutes
    }

    public void resetPassword(String token, String newPassword) {
        DecodedTokenDTO decoded = decodeToken(token);
        String username = decoded.getUsername(); // JWT payload includes username

        UserModel user = users.get(username);
        if (user == null) {
            throw new RuntimeException("Invalid token: user does not exist");
        }

        user.setPassword(newPassword); // In production, hash it
    }


}


