package com.accenture.backend.infrastructure.web.controller;

import com.accenture.backend.application.dto.request.CategoryRequest;
import com.accenture.backend.application.dto.response.CategoryResponse;
import com.accenture.backend.application.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/categories")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Operations related to task categories")
public class CategoryController {
    private final CategoryService service;

    @Operation(
            summary = "Create a new category",
            description = "Creates a new task category with the given name",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Category created successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CategoryResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CategoryResponse> create(@RequestBody CategoryRequest request) {
        return service.create(request);
    }


    @Operation(
            summary = "Get all categories",
            description = "Returns a list of all task categories",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of categories",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CategoryResponse.class)))
            }
    )
    @GetMapping
    public Flux<CategoryResponse> findAll() {
        return service.findAll();
    }


    @Operation(
            summary = "Get a category by ID",
            description = "Returns the category matching the given ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CategoryResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Category not found")
            }
    )
    @GetMapping("/{id}")
    public Mono<CategoryResponse> findById(@PathVariable("id") String id) {
        return service.findById(id);
    }


}