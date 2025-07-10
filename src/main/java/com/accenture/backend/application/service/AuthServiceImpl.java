package com.accenture.backend.application.service;

import com.accenture.backend.application.dto.request.LoginRequest;
import com.accenture.backend.application.dto.response.JwtResponse;
import com.accenture.backend.domain.model.User;
import com.accenture.backend.domain.repository.UserRepository;
import com.accenture.backend.infrastructure.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Mono<ResponseEntity<String>> register(LoginRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .flatMap(user -> Mono.just(ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Username already exists")))
                .switchIfEmpty(
                        userRepository.save(User.builder()
                                        .username(request.getUsername())
                                        .password(passwordEncoder.encode(request.getPassword()))
                                        .build())
                                .map(savedUser -> ResponseEntity
                                        .status(HttpStatus.CREATED)
                                        .body("User registered successfully"))
                );
    }

    @Override
    public Mono<ResponseEntity<JwtResponse>> login(LoginRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .flatMap(user -> {
                    if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                        String token = jwtUtil.generateToken(user.getUsername());
                        JwtResponse response = new JwtResponse(token);
                        return Mono.just(ResponseEntity.ok(response));
                    } else {
                        return Mono.just(ResponseEntity.status(401).body((JwtResponse) null));
                    }
                })
                .switchIfEmpty(Mono.just(ResponseEntity.status(401).body((JwtResponse) null)));
    }


}