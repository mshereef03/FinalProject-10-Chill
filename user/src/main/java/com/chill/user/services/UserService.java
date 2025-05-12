package com.chill.user.services;

import com.chill.user.JwtUtil;
import com.chill.user.dto.DecodedTokenDTO;
import com.chill.user.exceptions.InvalidCredentialsException;
import com.chill.user.models.UserModel;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final JwtUtil jwtUtil;

    public UserService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
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

}


