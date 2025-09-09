package com.DTechLtd.propertypulse_backend.dto;

public class AuthResponse {
    private String token;
    private String message;
    private String role; // added

    public AuthResponse(String token, String message, String role) {
        this.token = token;
        this.message = message;
        this.role = role;
    }

    // Getters
    public String getToken() { return token; }
    public String getMessage() { return message; }
    public String getRole() { return role; }
}
