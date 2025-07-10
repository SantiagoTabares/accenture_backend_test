package com.accenture.to_do_List_backend.infrastructure;

import com.accenture.to_do_List_backend.application.dto.request.LoginRequest;
import com.accenture.to_do_List_backend.application.dto.response.JwtResponse;
import com.accenture.to_do_List_backend.application.service.AuthService;
import com.accenture.to_do_List_backend.infrastructure.web.controller.AuthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    private AuthService authService;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class); // Mockito puro
        AuthController authController = new AuthController(authService);

        // Configura WebTestClient en modo standalone (sin contexto Spring completo)
        webTestClient = WebTestClient.bindToController(authController).build();
    }

    @Test
    void register_shouldReturnCreated() {
        LoginRequest request = new LoginRequest();
        request.setUsername("newUser");
        request.setPassword("1234");

        when(authService.register(any(LoginRequest.class)))
                .thenReturn(Mono.just(ResponseEntity.status(201).body("User registered successfully")));

        webTestClient.post()
                .uri("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class).isEqualTo("User registered successfully");
    }

    @Test
    void register_shouldReturnBadRequest_whenUsernameExists() {
        LoginRequest request = new LoginRequest();
        request.setUsername("existingUser");
        request.setPassword("1234");

        when(authService.register(any(LoginRequest.class)))
                .thenReturn(Mono.just(ResponseEntity.badRequest().body("Username already exists")));

        webTestClient.post()
                .uri("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("Username already exists");
    }


    @Test
    void login_shouldReturnUnauthorized_whenInvalidCredentials() {
        LoginRequest request = new LoginRequest();
        request.setUsername("invalidUser");
        request.setPassword("wrongPassword");

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(Mono.just(ResponseEntity.status(401).body(null)));

        webTestClient.post()
                .uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isUnauthorized();
    }
}