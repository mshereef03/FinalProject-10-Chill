package com.chill.user.dto;

import java.util.Date;
import java.util.List;

public class DecodedTokenDTO {
    private String userId;
    private String username;
    private List<String> roles;
    private Date issuedAt;
    private Date expiration;

    // Constructor
    public DecodedTokenDTO(String userId, String username, List<String> roles, Date issuedAt, Date expiration) {
        this.userId = userId;
        this.username = username;
        this.roles = roles;
        this.issuedAt = issuedAt;
        this.expiration = expiration;
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public Date getIssuedAt() {
        return issuedAt;
    }

    public Date getExpiration() {
        return expiration;
    }
}

