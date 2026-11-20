package com.careerflow.service;

import com.careerflow.dto.AuthResponse;
import com.careerflow.dto.LoginRequest;
import com.careerflow.dto.RegisterRequest;
import com.careerflow.dto.UserResponse;
import com.careerflow.entity.User;
import com.careerflow.exception.DuplicateEmailException;
import com.careerflow.exception.ResourceNotFoundException;
import com.careerflow.repository.UserRepository;
import com.careerflow.security.JwtUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/** Business logic for registration, login and "who am I". */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("An account with this email already exists");
        }

        User user = new User();
        user.setFullName(request.fullName().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.password())); // hash, never store plain text
        user.setCreatedAt(LocalDateTime.now());
        User saved = userRepository.save(user);

        return new AuthResponse(jwtUtil.generateToken(saved.getId()), saved.getFullName(), saved.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.email().trim().toLowerCase();

        // Same error for "no such email" and "wrong password" so attackers learn nothing
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return new AuthResponse(jwtUtil.generateToken(user.getId()), user.getFullName(), user.getEmail());
    }

    public UserResponse getCurrentUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserResponse.from(user);
    }
}
