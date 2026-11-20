package com.careerflow.dto;

import com.careerflow.entity.User;

import java.time.LocalDateTime;

/** Safe view of a user (never includes the password hash). */
public record UserResponse(String id, String fullName, String email, LocalDateTime createdAt) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getCreatedAt());
    }
}
