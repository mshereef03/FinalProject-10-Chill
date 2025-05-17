package com.chill.user.services;

import com.chill.user.JwtUtil;
import com.chill.user.dto.DecodedTokenDTO;
import com.chill.user.exceptions.InvalidCredentialsException;
import com.chill.user.models.UserModel;
import com.chill.user.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import com.chill.user.factory.*;

@Service
public class UserService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public UserService(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
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

    //  CRUD logic
    public UserModel createUser(UserModel user) {
        try {
            // Extract role from the incoming request
            String rawRole = user.getRoles().get(0);  // e.g., ROLE_ADMIN
            String typeName = rawRole.replace("ROLE_", "").toLowerCase();  // e.g., admin

            // Capitalize first letter and append 'UserCreator'
            String className = typeName.substring(0, 1).toUpperCase() + typeName.substring(1) + "UserCreator";

            // Fully qualified class name
            String fullClassName = "com.chill.user.factory." + className;

            // Use reflection to load and instantiate the UserCreator
            Class<?> clazz = Class.forName(fullClassName);
            UserCreator creator = (UserCreator) clazz.getDeclaredConstructor().newInstance();

            // Use the factory method pattern
            UserModel preparedUser = creator.create(user);
            return userRepository.save(preparedUser);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create user with role: " + user.getRoles().get(0), e);
        }
    }


    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserModel> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public UserModel updateUser(Long id, UserModel updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(updatedUser.getUsername());
                    user.setPassword(updatedUser.getPassword());
                    user.setRoles(updatedUser.getRoles());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


    public String requestPasswordReset(String email) {

        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("User with email not found"));

        return jwtUtil.generateResetToken(user); // This token will expire in e.g., 10 minutes
    }

    public void resetPassword(String token, String newPassword) {
        DecodedTokenDTO decoded = decodeToken(token);
        String username = decoded.getUsername(); // JWT payload includes username

        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("User with username not found"));

        user.setPassword(newPassword); // In production, hash it
        userRepository.save(user);
    }


}


