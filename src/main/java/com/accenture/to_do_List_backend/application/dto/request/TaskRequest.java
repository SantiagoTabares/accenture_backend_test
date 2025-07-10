package com.accenture.to_do_List_backend.application.dto.request;

public record TaskRequest(
        String title,
        String description,
        boolean completed,
        String categoryId
) {}