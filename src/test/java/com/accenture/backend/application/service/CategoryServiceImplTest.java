package com.accenture.backend.application.service;

import com.accenture.backend.application.dto.request.CategoryRequest;
import com.accenture.backend.domain.model.Category;
import com.accenture.backend.domain.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void create_WhenCategoryDoesNotExist_ShouldCreateSuccessfully() {
        // Arrange
        CategoryRequest request = new CategoryRequest("Work");
        Category savedCategory = Category.builder().id("1").name("Work").build();

        when(repository.existsByName("Work")).thenReturn(Mono.just(false));
        when(repository.save(any(Category.class))).thenReturn(Mono.just(savedCategory));

        // Act & Assert
        StepVerifier.create(categoryService.create(request))
                .expectNextMatches(response ->
                        response.id().equals("1") &&
                                response.name().equals("Work"))
                .verifyComplete();
    }

    @Test
    void create_WhenCategoryExists_ShouldThrowException() {
        // Arrange
        CategoryRequest request = new CategoryRequest("Work");

        when(repository.existsByName("Work")).thenReturn(Mono.just(true));

        // Act & Assert
        StepVerifier.create(categoryService.create(request))
                .expectErrorMatches(ex ->
                        ex instanceof IllegalArgumentException &&
                                ex.getMessage().equals("Category already exists"))
                .verify();
    }

    @Test
    void findAll_ShouldReturnAllCategories() {
        // Arrange
        Category category1 = Category.builder().id("1").name("Work").build();
        Category category2 = Category.builder().id("2").name("Personal").build();

        when(repository.findAll()).thenReturn(Flux.just(category1, category2));

        // Act & Assert
        StepVerifier.create(categoryService.findAll())
                .expectNextMatches(response ->
                        response.id().equals("1") &&
                                response.name().equals("Work"))
                .expectNextMatches(response ->
                        response.id().equals("2") &&
                                response.name().equals("Personal"))
                .verifyComplete();
    }

    @Test
    void findById_WhenCategoryExists_ShouldReturnCategory() {
        // Arrange
        String categoryId = "1";
        Category category = Category.builder().id(categoryId).name("Work").build();

        when(repository.findById(categoryId)).thenReturn(Mono.just(category));

        // Act & Assert
        StepVerifier.create(categoryService.findById(categoryId))
                .expectNextMatches(response ->
                        response.id().equals(categoryId) &&
                                response.name().equals("Work"))
                .verifyComplete();
    }

    @Test
    void findById_WhenCategoryNotExists_ShouldThrowNotFoundException() {
        // Arrange
        String categoryId = "999";

        when(repository.findById(categoryId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(categoryService.findById(categoryId))
                .expectErrorMatches(ex ->
                        ex instanceof ResponseStatusException &&
                                ((ResponseStatusException) ex).getStatusCode() == HttpStatus.NOT_FOUND &&
                                ex.getMessage().contains("Category not found"))
                .verify();
    }
}