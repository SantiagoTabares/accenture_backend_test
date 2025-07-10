package com.accenture.backend.application.service;

import com.accenture.backend.application.dto.request.CategoryRequest;
import com.accenture.backend.application.dto.response.CategoryResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoryService {
    Mono<CategoryResponse> create(CategoryRequest request);
    Flux<CategoryResponse> findAll();
    Mono<CategoryResponse> findById(String id);

}