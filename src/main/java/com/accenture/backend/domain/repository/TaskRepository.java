package com.accenture.backend.domain.repository;

import com.accenture.backend.domain.model.Category;
import com.accenture.backend.domain.model.Franchise;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface TaskRepository extends ReactiveMongoRepository<Franchise, String> {

    Flux<Franchise> findByCompleted(boolean completed);
    Flux<Franchise> findByCategory(Category category); // Filtro por ID de categoría
}