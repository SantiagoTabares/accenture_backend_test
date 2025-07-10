package com.accenture.backend.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO used to create a new category")
public record CategoryRequest(
        @NotBlank(message = "Category name must not be blank")
        String name
) {}