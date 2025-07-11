package com.accenture.backend.domain.repository;

import com.accenture.backend.domain.model.Sucursal;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface SucursalRepository extends ReactiveMongoRepository<Sucursal, String> {
}