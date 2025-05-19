package com.chill.user.security.strategy;

import com.chill.user.dto.LoginRequestDTO;
import com.chill.user.exceptions.InvalidCredentialsException;
import com.chill.user.model.UserModel;
import com.chill.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UsernamePasswordStrategy implements AuthenticationStrategy {

    private final UserRepository userRepo;

    public UsernamePasswordStrategy(UserRepository userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    public UserModel authenticate(LoginRequestDTO req) {
        // 1) Load user or fail
        UserModel user = userRepo.findByUsername(req.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        // 2) Raw password check
        if (!req.getPassword().equals(user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        // 3) Success
        return user;
    }
}
