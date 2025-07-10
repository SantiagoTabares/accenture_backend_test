package com.accenture.to_do_List_backend.application.service;


import com.accenture.to_do_List_backend.application.dto.request.CategoryRequest;
import com.accenture.to_do_List_backend.application.dto.response.CategoryResponse;
import com.accenture.to_do_List_backend.domain.model.Category;
import com.accenture.to_do_List_backend.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    @Override
    public Mono<CategoryResponse> create(CategoryRequest request) {
        return repository.existsByName(request.name())
                .flatMap(exists -> exists ?
                        Mono.error(new IllegalArgumentException("Category already exists")) :
                        repository.save(Category.builder()
                                .name(request.name())
                                .build())
                )
                .map(this::toResponse);
    }

    @Override
    public Flux<CategoryResponse> findAll() {
        return repository.findAll().map(this::toResponse);
    }

    @Override
    public Mono<CategoryResponse> findById(String id) {
        return repository.findById(id)
                .map(this::toResponse)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found")
                ));
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName()
        );
    }
}