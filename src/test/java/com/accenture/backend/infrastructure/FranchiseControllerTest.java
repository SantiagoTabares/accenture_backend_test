package com.accenture.backend.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FranchiseControllerTest {

    private TaskService taskService;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        taskService = Mockito.mock(TaskService.class);
        TaskController controller = new TaskController(taskService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void create_shouldReturnCreatedTask() {
        TaskRequest request = new TaskRequest("Title", "Description", false, "cat-1");
        TaskResponse response = new TaskResponse("1", "Title", "Description", false,
                new CategoryResponse("cat-1", "Category"));

        when(taskService.create(any())).thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TaskResponse.class)
                .isEqualTo(response);
    }

    @Test
    void update_shouldReturnUpdatedTask() {
        String id = "1";
        TaskRequest request = new TaskRequest("Updated", "New Description", true, "cat-1");
        TaskResponse response = new TaskResponse(id, "Updated", "New Description", true,
                new CategoryResponse("cat-1", "Category"));

        when(taskService.update(Mockito.eq(id), any())).thenReturn(Mono.just(response));

        webTestClient.put()
                .uri("/api/v1/tasks/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .isEqualTo(response);
    }

    @Test
    void findAll_shouldReturnTasks() {
        TaskResponse response = new TaskResponse("1", "Title", "Description", false,
                new CategoryResponse("cat-1", "Category"));

        when(taskService.findAll()).thenReturn(Flux.just(response));

        webTestClient.get()
                .uri("/api/v1/tasks")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TaskResponse.class)
                .hasSize(1)
                .contains(response);
    }

    @Test
    void findById_shouldReturnTask() {
        String id = "1";
        TaskResponse response = new TaskResponse(id, "Title", "Description", false,
                new CategoryResponse("cat-1", "Category"));

        when(taskService.findById(id)).thenReturn(Mono.just(response));

        webTestClient.get()
                .uri("/api/v1/tasks/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .isEqualTo(response);
    }

    @Test
    void findByCategory_shouldReturnTasks() {
        String categoryId = "cat-1";
        TaskResponse response = new TaskResponse("1", "Title", "Description", false,
                new CategoryResponse(categoryId, "Category"));

        when(taskService.findByCategory(categoryId)).thenReturn(Flux.just(response));

        webTestClient.get()
                .uri("/api/v1/tasks/category/{categoryId}", categoryId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TaskResponse.class)
                .hasSize(1)
                .contains(response);
    }

    @Test
    void delete_shouldReturnNoContent() {
        String id = "1";
        when(taskService.delete(id)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/tasks/{id}", id)
                .exchange()
                .expectStatus().isNoContent();
    }
}
