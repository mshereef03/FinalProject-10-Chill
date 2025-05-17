package com.chill.user.security.strategy;

import com.chill.user.dto.LoginRequestDTO;
import com.chill.user.exceptions.InvalidCredentialsException;
import com.chill.user.model.UserModel;

public interface AuthenticationStrategy {

    UserModel authenticate(LoginRequestDTO request) throws InvalidCredentialsException;

}
