package com.accenture.to_do_List_backend.infrastructure;

import com.accenture.to_do_List_backend.application.dto.request.CategoryRequest;
import com.accenture.to_do_List_backend.application.dto.response.CategoryResponse;
import com.accenture.to_do_List_backend.application.service.CategoryService;
import com.accenture.to_do_List_backend.infrastructure.web.controller.CategoryController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@ContextConfiguration(classes = { CategoryController.class, CategoryControllerTest.TestConfig.class })
class CategoryControllerTest {

    private WebTestClient webTestClient;
    private static CategoryService categoryService = Mockito.mock(CategoryService.class);

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(new CategoryController(categoryService)).build();
    }

    @Test
    void create_shouldReturnCreatedCategory() {
        CategoryRequest request = new CategoryRequest("Work");
        CategoryResponse response = new CategoryResponse("1", "Work");

        Mockito.when(categoryService.create(Mockito.any(CategoryRequest.class)))
                .thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CategoryResponse.class)
                .value(res -> {
                    assert res.id().equals("1");
                    assert res.name().equals("Work");
                });
    }

    @Test
    void findAll_shouldReturnListOfCategories() {
        List<CategoryResponse> categories = List.of(
                new CategoryResponse("1", "Work"),
                new CategoryResponse("2", "Personal")
        );

        Mockito.when(categoryService.findAll())
                .thenReturn(Flux.fromIterable(categories));

        webTestClient.get()
                .uri("/api/v1/categories")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CategoryResponse.class)
                .hasSize(2)
                .value(list -> {
                    assert list.get(0).name().equals("Work");
                    assert list.get(1).name().equals("Personal");
                });
    }

    @Test
    void findById_shouldReturnCategory() {
        String id = "1";
        CategoryResponse response = new CategoryResponse(id, "Work");

        Mockito.when(categoryService.findById(id)).thenReturn(Mono.just(response));

        webTestClient.get()
                .uri("/api/v1/categories/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CategoryResponse.class)
                .value(res -> {
                    assert res.id().equals(id);
                    assert res.name().equals("Work");
                });
    }

    @Configuration
    static class TestConfig {
        @Bean
        public CategoryService categoryService() {
            return categoryService;
        }
    }
}
