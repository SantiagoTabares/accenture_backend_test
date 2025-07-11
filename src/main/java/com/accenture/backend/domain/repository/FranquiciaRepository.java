package com.accenture.backend.domain.repository;

import com.accenture.backend.domain.model.Franquicia;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface FranquiciaRepository extends ReactiveMongoRepository<Franquicia, String> {
}