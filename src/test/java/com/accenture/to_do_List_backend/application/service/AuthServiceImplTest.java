package com.accenture.to_do_List_backend.application.service;

import com.accenture.to_do_List_backend.application.dto.request.LoginRequest;

import com.accenture.to_do_List_backend.domain.model.User;
import com.accenture.to_do_List_backend.domain.repository.UserRepository;
import com.accenture.to_do_List_backend.infrastructure.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_shouldCreateUser_whenUsernameDoesNotExist() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsername("newUser");
        request.setPassword("password");

        when(userRepository.findByUsername("newUser"))
                .thenReturn(Mono.empty());

        when(userRepository.save(any(User.class)))
                .thenReturn(Mono.just(User.builder()
                        .username("newUser")
                        .password("encryptedPassword")
                        .build()));

        // Act & Assert
        StepVerifier.create(authService.register(request))
                .expectNextMatches(response ->
                        response.getStatusCodeValue() == 201 &&
                                "User registered successfully".equals(response.getBody()))
                .verifyComplete();

        verify(userRepository).save(any(User.class));
    }

    // Test para registrar cuando es un nuevo usuario
    @Test
    void register_shouldCreateUser_whenUsernameIsAvailable() {
        LoginRequest request = new LoginRequest("newUser", "password");

        when(userRepository.findByUsername("newUser")).thenReturn(Mono.empty());
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(authService.register(request))
                .expectNextMatches(response ->
                        response.getStatusCodeValue() == 201 &&
                                response.getBody().equals("User registered successfully"))
                .verifyComplete();

        verify(userRepository).save(userCaptor.capture());
        assertEquals("newUser", userCaptor.getValue().getUsername());
        assert passwordEncoder.matches("password", userCaptor.getValue().getPassword());
    }

    // Test para login exitoso
    @Test
    void login_shouldReturnJwtResponse_whenCredentialsAreValid() {
        String rawPassword = "validPass";
        String hashedPassword = passwordEncoder.encode(rawPassword);
        LoginRequest request = new LoginRequest("validUser", rawPassword);

        User user = User.builder().username("validUser").password(hashedPassword).build();

        when(userRepository.findByUsername("validUser")).thenReturn(Mono.just(user));
        when(jwtUtil.generateToken("validUser")).thenReturn("mockedToken");

        StepVerifier.create(authService.login(request))
                .expectNextMatches(response ->
                        response.getStatusCodeValue() == 200 &&
                                response.getBody() != null &&
                                response.getBody().getToken().equals("mockedToken"))
                .verifyComplete();
    }

    // Test para login con contraseña incorrecta
    @Test
    void login_shouldReturnUnauthorized_whenPasswordIsInvalid() {
        LoginRequest request = new LoginRequest("user", "wrongPassword");
        String hashedPassword = passwordEncoder.encode("correctPassword");

        User user = User.builder().username("user").password(hashedPassword).build();
        when(userRepository.findByUsername("user")).thenReturn(Mono.just(user));

        StepVerifier.create(authService.login(request))
                .expectNextMatches(response ->
                        response.getStatusCodeValue() == 401 && response.getBody() == null)
                .verifyComplete();
    }

    // Test para login con usuario inexistente
    @Test
    void login_shouldReturnUnauthorized_whenUserNotFound() {
        LoginRequest request = new LoginRequest("nonExistentUser", "pass");
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Mono.empty());

        StepVerifier.create(authService.login(request))
                .expectNextMatches(response ->
                        response.getStatusCodeValue() == 401 && response.getBody() == null)
                .verifyComplete();
    }
}
