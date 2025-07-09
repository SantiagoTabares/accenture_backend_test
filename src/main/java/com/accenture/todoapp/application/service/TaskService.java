package com.accenture.todoapp.application.service;

import com.accenture.todoapp.application.dto.request.TaskRequest;
import com.accenture.todoapp.application.dto.response.TaskResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {
    Mono<TaskResponse> create(TaskRequest request);
    Flux<TaskResponse> findAll();
    Mono<TaskResponse> update(String id, TaskRequest request);
    Mono<Void> delete(String id);
    Flux<TaskResponse> findByCategory(String categoryId);
    Mono<TaskResponse> findById(String id);
}