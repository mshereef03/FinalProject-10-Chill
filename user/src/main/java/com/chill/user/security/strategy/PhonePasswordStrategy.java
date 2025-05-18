package com.chill.user.security.strategy;

import com.chill.user.dto.LoginRequestDTO;
import com.chill.user.exceptions.InvalidCredentialsException;
import com.chill.user.model.UserModel;
import com.chill.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class PhonePasswordStrategy implements AuthenticationStrategy {

    private final UserRepository userRepo;

    public PhonePasswordStrategy(UserRepository userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    public UserModel authenticate(LoginRequestDTO req) {
        // 1) Load user or fail
        UserModel user = userRepo.findByPhoneNumber(req.getPhoneNumber())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid Phone Number or password"));

        // 2) Raw password check
        if (!req.getPassword().equals(user.getPassword())) {
            throw new InvalidCredentialsException("Invalid Phone Number or password");
        }

        // 3) Success
        return user;
    }
}
