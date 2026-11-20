package com.careerflow.dto;

/** Returned after register/login. The frontend stores the token. */
public record AuthResponse(String token, String fullName, String email) {
}
