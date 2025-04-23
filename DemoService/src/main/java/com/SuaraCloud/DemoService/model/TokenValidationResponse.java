package com.SuaraCloud.DemoService.model;

public class TokenValidationResponse {
    private boolean valid;
    private String userId;

    // Default constructor needed for Jackson deserialization
    public TokenValidationResponse() {
    }

    public TokenValidationResponse(boolean valid, String userId) {
        this.valid = valid;
        this.userId = userId;
    }

    // Getters and setters
    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
