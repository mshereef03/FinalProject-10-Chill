package com.chill.user.dto;


public class CustomErrorDTO {
    private String error;
    private String message;

    public CustomErrorDTO(String error, String message) {
        this.error = error;
        this.message = message;
    }
    public String getError()   { return error; }
    public String getMessage() { return message; }
}

