package com.pensun.inspection_app.model;

public class LoginResponse {
    private String token;
    private User user;

    // Getters and setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
} 