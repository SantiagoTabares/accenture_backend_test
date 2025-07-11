package com.accenture.backend.application.service;

import com.accenture.backend.application.dto.request.LoginRequest;
import com.accenture.backend.application.dto.response.JwtResponse;
import com.accenture.backend.domain.model.User;
import com.accenture.backend.domain.repository.UserRepository;
import com.accenture.backend.infrastructure.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_PASSWORD = "testPass123";
    private static final String ENCRYPTED_PASSWORD = "$2a$10$N9qo8uLOickgx2ZMRZoMy.Mrq4H8/3WUj.5YJ5J8v8GFjQX/6zUOa"; // testPass123 encrypted
    private static final String JWT_TOKEN = "mocked.jwt.token";

    @Test
    void register_shouldSucceedWhenUsernameAvailable() {
        // Arrange
        LoginRequest request = new LoginRequest(TEST_USERNAME, TEST_PASSWORD);

        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Mono.empty());
        when(userRepository.save(any(User.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        // Act & Assert
        StepVerifier.create(authService.register(request))
                .expectNextMatches(response ->
                        response.getStatusCode() == HttpStatus.CREATED &&
                                response.getBody().equals("User registered successfully"))
                .verifyComplete();

        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals(TEST_USERNAME, savedUser.getUsername());
        assertTrue(passwordEncoder.matches(TEST_PASSWORD, savedUser.getPassword()));
    }

    @Test
    void register_shouldFailWhenUsernameExists() {
        // Arrange
        LoginRequest request = new LoginRequest(TEST_USERNAME, TEST_PASSWORD);
        User existingUser = User.builder().username(TEST_USERNAME).build();
        // Configura el mock para devolver un usuario existente
        when(userRepository.findByUsername(TEST_USERNAME))
                .thenReturn(Mono.just(existingUser));


        // Act & Assert
        StepVerifier.create(authService.register(request))
                .expectNextMatches(response ->
                        response.getStatusCode() == HttpStatus.BAD_REQUEST &&
                                response.getBody().equals("Username already exists"))
                .verifyComplete();

        verify(userRepository, never()).save(any());
    }

    @Test
    void login_shouldReturnJwtWhenCredentialsValid() {
        // Arrange
        String rawPassword = "testPass123"; // Contraseña en texto plano
        String encryptedPassword = passwordEncoder.encode(rawPassword); // Contraseña encriptada

        LoginRequest request = new LoginRequest(TEST_USERNAME, rawPassword);
        User user = User.builder()
                .username(TEST_USERNAME)
                .password(encryptedPassword) // Usa la contraseña encriptada
                .build();

        // Configura los mocks
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Mono.just(user));
        when(jwtUtil.generateToken(TEST_USERNAME)).thenReturn(JWT_TOKEN);

        // Act & Assert
        StepVerifier.create(authService.login(request))
                .expectNextMatches(response ->
                        response.getStatusCode() == HttpStatus.OK &&
                                response.getBody() != null &&
                                response.getBody().getToken().equals(JWT_TOKEN))
                .verifyComplete();
    }

    @Test
    void login_shouldFailWhenUserNotFound() {
        // Arrange
        LoginRequest request = new LoginRequest(TEST_USERNAME, TEST_PASSWORD);

        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(authService.login(request))
                .expectNextMatches(response ->
                        response.getStatusCode() == HttpStatus.UNAUTHORIZED &&
                                response.getBody() == null)
                .verifyComplete();
    }

    @Test
    void login_shouldFailWhenPasswordInvalid() {
        // Arrange
        LoginRequest request = new LoginRequest(TEST_USERNAME, "wrongPassword");
        User user = User.builder()
                .username(TEST_USERNAME)
                .password(ENCRYPTED_PASSWORD)
                .build();

        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Mono.just(user));

        // Act & Assert
        StepVerifier.create(authService.login(request))
                .expectNextMatches(response ->
                        response.getStatusCode() == HttpStatus.UNAUTHORIZED &&
                                response.getBody() == null)
                .verifyComplete();
    }
}