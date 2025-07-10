package com.accenture.todoapp.application.service;

import com.accenture.todoapp.application.dto.request.LoginRequest;
import com.accenture.todoapp.application.dto.response.JwtResponse;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<ResponseEntity<String>> register(LoginRequest request);
    Mono<ResponseEntity<JwtResponse>> login(LoginRequest request);
}
