package com.accenture.todoapp.infrastructure.web.controller;


import com.accenture.todoapp.application.dto.request.LoginRequest;
import com.accenture.todoapp.application.dto.response.JwtResponse;
import com.accenture.todoapp.application.service.AuthService;
import com.accenture.todoapp.infrastructure.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user login and JWT generation")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user with encoded password")
    public Mono<ResponseEntity<String>> register(@RequestBody LoginRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Validates credentials and returns a JWT token")
    public Mono<ResponseEntity<JwtResponse>> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
