package com.pensun.inspection_app.model;

public class LogoutResponse {
    private boolean success;
    private String message;

    // Getters and setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
} 