package com.accenture.backend.infrastructure.controller;

import com.accenture.backend.application.dto.request.LoginRequest;
import com.accenture.backend.application.dto.response.JwtResponse;
import com.accenture.backend.application.service.interfaces.AuthService;
import com.accenture.backend.infrastructure.web.controller.AuthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private static final String REGISTER_PATH = "/api/v1/auth/register";
    private static final String LOGIN_PATH = "/api/v1/auth/login";
    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_PASSWORD = "testPass123";
    private static final String SUCCESS_MESSAGE = "User registered successfully";
    private static final String USER_EXISTS_MESSAGE = "Username already exists";
    private static final String AUTH_TOKEN = "mockAuthToken";

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(authController).build();
    }

    @Test
    void register_shouldReturnCreated() {
        // Arrange
        LoginRequest request = createLoginRequest();
        when(authService.register(any(LoginRequest.class)))
                .thenReturn(Mono.just(ResponseEntity.status(CREATED).body(SUCCESS_MESSAGE)));

        // Act & Assert
        webTestClient.post()
                .uri(REGISTER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class).isEqualTo(SUCCESS_MESSAGE);
    }

    @Test
    void register_shouldReturnBadRequest_whenUsernameExists() {
        // Arrange
        LoginRequest request = createLoginRequest();
        when(authService.register(any(LoginRequest.class)))
                .thenReturn(Mono.just(ResponseEntity.badRequest().body(USER_EXISTS_MESSAGE)));

        // Act & Assert
        webTestClient.post()
                .uri(REGISTER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo(USER_EXISTS_MESSAGE);
    }


    @Test
    void login_shouldReturnUnauthorized_whenInvalidCredentials() {
        // Arrange
        LoginRequest request = createLoginRequest();
        when(authService.login(any(LoginRequest.class)))
                .thenReturn(Mono.just(ResponseEntity.status(UNAUTHORIZED).build()));

        // Act & Assert
        webTestClient.post()
                .uri(LOGIN_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody().isEmpty();
    }

    private LoginRequest createLoginRequest() {
        LoginRequest request = new LoginRequest();
        request.setUsername(TEST_USERNAME);
        request.setPassword(TEST_PASSWORD);
        return request;
    }
}