package com.accenture.to_do_List_backend.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO used to return category information")
public record CategoryResponse(
        @Schema(description = "Unique identifier of the category", example = "64aef1f2b23a9a001e5c77af")
        String id,

        @Schema(description = "Name of the category", example = "Work")
        String name
) {}