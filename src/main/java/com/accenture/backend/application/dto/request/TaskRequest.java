package com.accenture.backend.application.dto.request;

public record TaskRequest(
        String title,
        String description,
        boolean completed,
        String categoryId
) {}