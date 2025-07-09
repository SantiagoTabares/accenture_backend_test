package com.accenture.todoapp.application.dto.request;


public record TaskRequest(
        String title,
        String description,
        boolean completed,
        String categoryId
) {}