package com.chill.user.service;

import com.chill.user.security.strategy.AuthenticationStrategy;
import com.chill.user.security.JwtUtil;
import com.chill.user.security.strategy.PhonePasswordStrategy;
import com.chill.user.security.strategy.UsernamePasswordStrategy;
import com.chill.user.dto.DecodedTokenDTO;
import com.chill.user.dto.LoginRequestDTO;
import com.chill.user.exceptions.InvalidCredentialsException;
import com.chill.user.model.UserModel;
import com.chill.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import com.chill.user.factory.*;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final JwtUtil jwtUtil;
    private final UsernamePasswordStrategy userStrategy;
    private final PhonePasswordStrategy phoneStrategy;
    private final UserRepository userRepository;

    public UserService(JwtUtil jwtUtil, UserRepository userRepository, UsernamePasswordStrategy userStrategy, PhonePasswordStrategy phoneStrategy) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.userStrategy = userStrategy;
        this.phoneStrategy = phoneStrategy;
    }

    public String login(LoginRequestDTO req) {
        AuthenticationStrategy strat =
                req.isUsePhoneLogin() ? phoneStrategy : userStrategy;

        UserModel user = strat.authenticate(req);
        return jwtUtil.generateToken(user);
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
            System.out.println(preparedUser.getEmail());
            System.out.println(preparedUser.getPassword());
            System.out.println(preparedUser.getPhoneNumber());
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

    public String requestEmailVerification(String loginToken){
        DecodedTokenDTO decoded = decodeToken(loginToken);
        String username = decoded.getUsername();

        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("User with username not found"));

        return jwtUtil.generateEmailToken(user);
    }

    public void verifyEmail(String emailToken){
        DecodedTokenDTO decoded = decodeToken(emailToken);
        String username = decoded.getUsername();

        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("User with username not found"));

        user.setEmailVerified(true);
        userRepository.save(user);
    }


    @Transactional
    public void clearDatabase() {
        userRepository.deleteAllInBatch();
    }
}


