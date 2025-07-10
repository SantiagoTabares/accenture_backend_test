package com.accenture.to_do_List_backend.application.dto.response;



public record TaskResponse(
        String id,
        String title,
        String description,
        boolean completed,
        CategoryResponse category
) {}