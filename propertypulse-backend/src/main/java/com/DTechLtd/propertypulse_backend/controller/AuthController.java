package com.DTechLtd.propertypulse_backend.controller;

import com.DTechLtd.propertypulse_backend.dto.*;
import com.DTechLtd.propertypulse_backend.entity.Role;
import com.DTechLtd.propertypulse_backend.entity.UserEntity;
import com.DTechLtd.propertypulse_backend.repository.UserRepository;
import com.DTechLtd.propertypulse_backend.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    // ---------------------------
    // REGISTER USER
    // ---------------------------
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already registered");
        }

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Assign role
        if (request.getRole() != null) {
            try {
                user.setRole(Role.valueOf(request.getRole().toUpperCase()));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid role. Use USER, ADMIN, or AGENT.");
            }
        } else {
            user.setRole(Role.USER); // default role
        }

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully with role: " + user.getRole());
    }

    // ---------------------------
    // LOGIN USER
    // ---------------------------
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Generate JWT including username (and optionally role later)
        String token = jwtUtil.generateToken(authentication.getName());

        UserEntity user = userRepository.findByEmail(request.getEmail()).orElse(null);
        String role = (user != null && user.getRole() != null) ? user.getRole().name() : "USER";

        return ResponseEntity.ok(new AuthResponse(token, "Login successful", role));
    }
}
