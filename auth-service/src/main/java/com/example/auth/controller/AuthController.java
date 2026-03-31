package com.example.auth.controller;

import com.example.auth.dto.request.AuthRequest;
import com.example.auth.dto.response.AuthResponse;
import com.example.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody AuthRequest request) {
        // By default assign USER role, for simplicity. If they need ADMIN, they can provide it or we can hardcode logic
        return ResponseEntity.ok(authService.register(request, "ROLE_USER"));
    }

    @PostMapping("/signup/admin")
    public ResponseEntity<AuthResponse> signupAdmin(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.register(request, "ROLE_ADMIN"));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
