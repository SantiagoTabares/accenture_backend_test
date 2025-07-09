package com.accenture.todoapp.infrastructure.web.controller;

import com.accenture.todoapp.application.dto.request.TaskRequest;
import com.accenture.todoapp.application.dto.response.TaskResponse;
import com.accenture.todoapp.application.service.TaskService;


import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TaskResponse> create(@RequestBody TaskRequest request) {
        return taskService.create(request);
    }

    @GetMapping
    public Flux<TaskResponse> findAll() {
        return taskService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<TaskResponse> findById(@PathVariable("id") String categoryId) {
        return taskService.findById(categoryId);
    }

    @GetMapping("/category/{categoryId}")
    public Flux<TaskResponse> findByCategory(@PathVariable("categoryId") String categoryId) {
        return taskService.findByCategory(categoryId);
    }

    @PutMapping("/{id}")
    public Mono<TaskResponse> update(@PathVariable("id") String id, @RequestBody TaskRequest request) {
        return taskService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable("id") String id) {
        return taskService.delete(id);
    }
}