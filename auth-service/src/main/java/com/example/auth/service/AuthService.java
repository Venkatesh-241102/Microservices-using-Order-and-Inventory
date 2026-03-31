package com.example.auth.service;

import com.example.auth.dto.request.AuthRequest;
import com.example.auth.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(AuthRequest request, String role);
    AuthResponse login(AuthRequest request);
}
