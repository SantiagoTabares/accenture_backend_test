package com.accenture.to_do_List_backend.infrastructure.web.controller;

import com.accenture.to_do_List_backend.application.dto.request.TaskRequest;
import com.accenture.to_do_List_backend.application.dto.response.TaskResponse;
import com.accenture.to_do_List_backend.application.service.TaskService;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Operations related to task management")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Create a new task", responses = {
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TaskResponse> create(@RequestBody TaskRequest request) {
        return taskService.create(request);
    }

    @Operation(summary = "Update an existing task", responses = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/{id}")
    public Mono<TaskResponse> update(@PathVariable("id") String id, @RequestBody TaskRequest request) {
        return taskService.update(id, request);
    }

    @Operation(summary = "Get all tasks")
    @GetMapping
    public Flux<TaskResponse> findAll() {
        return taskService.findAll();
    }


    @Operation(summary = "Find a task by its ID", responses = {
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{id}")
    public Mono<TaskResponse> findById(@PathVariable("id") String categoryId) {
        return taskService.findById(categoryId);
    }

    @Operation(summary = "Get tasks by category ID")
    @GetMapping("/category/{categoryId}")
    public Flux<TaskResponse> findByCategory(@PathVariable("categoryId") String categoryId) {
        return taskService.findByCategory(categoryId);
    }


    @Operation(summary = "Delete a task by ID", responses = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable("id") String id) {
        return taskService.delete(id);
    }
}