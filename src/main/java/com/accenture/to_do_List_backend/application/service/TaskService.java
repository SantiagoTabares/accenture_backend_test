package com.accenture.to_do_List_backend.application.service;

import com.accenture.to_do_List_backend.application.dto.request.TaskRequest;
import com.accenture.to_do_List_backend.application.dto.response.TaskResponse;

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