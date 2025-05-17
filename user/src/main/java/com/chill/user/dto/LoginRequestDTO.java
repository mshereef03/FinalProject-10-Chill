package com.chill.user.dto;

public class LoginRequestDTO {

    private String username;
    private String phoneNumber;
    private String password;
    private boolean usePhoneLogin;

    // Getters and Setters
    public LoginRequestDTO(){}

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public boolean isUsePhoneLogin() {
        return usePhoneLogin;
    }


    public void setUsePhoneLogin(boolean usePhoneLogin) {
        this.usePhoneLogin = usePhoneLogin;
    }


}

