package com.chill.user.dto;

public class VerifyEmailDTO {


    private String token;

    public VerifyEmailDTO(String token) {
        this.token = token;
    }

    // Getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
