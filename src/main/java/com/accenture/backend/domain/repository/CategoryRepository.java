package com.accenture.backend.domain.repository;

import com.accenture.backend.domain.model.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
    Mono<Boolean> existsByName(String name);
}