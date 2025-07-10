package com.accenture.to_do_List_backend.domain.repository;

import com.accenture.to_do_List_backend.domain.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findByUsername(String username);
}