package com.chill.user.exceptions;

import com.chill.user.dto.CustomErrorDTO;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<CustomErrorDTO> handleJwt(JwtException ex) {
        CustomErrorDTO body = new CustomErrorDTO("invalid_token", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(body);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<CustomErrorDTO> handleCredentials(InvalidCredentialsException ex) {
        CustomErrorDTO body = new CustomErrorDTO("invalid_credentials", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(body);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CustomErrorDTO> handleOther(RuntimeException ex) {
        CustomErrorDTO body = new CustomErrorDTO("server_error", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }
}
