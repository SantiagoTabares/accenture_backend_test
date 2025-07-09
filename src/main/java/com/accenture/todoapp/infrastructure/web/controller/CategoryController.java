package com.accenture.todoapp.infrastructure.web.controller;

import com.accenture.todoapp.application.dto.request.CategoryRequest;
import com.accenture.todoapp.application.dto.response.CategoryResponse;
import com.accenture.todoapp.application.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CategoryResponse> create(@RequestBody CategoryRequest request) {
        return service.create(request);
    }

    @GetMapping
    public Flux<CategoryResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Mono<CategoryResponse> findById(@PathVariable("id") String id) {
        return service.findById(id);
    }




}