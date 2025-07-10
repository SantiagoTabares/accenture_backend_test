package com.accenture.to_do_List_backend.application.service;

import com.accenture.to_do_List_backend.application.dto.request.LoginRequest;
import com.accenture.to_do_List_backend.application.dto.response.JwtResponse;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<ResponseEntity<String>> register(LoginRequest request);
    Mono<ResponseEntity<JwtResponse>> login(LoginRequest request);
}
