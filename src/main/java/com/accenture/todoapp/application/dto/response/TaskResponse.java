package com.accenture.todoapp.application.dto.response;


public record TaskResponse(
        String id,
        String title,
        String description,
        boolean completed,
        CategoryResponse category
) {}