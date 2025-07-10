package com.accenture.backend.application.service;

import com.accenture.backend.domain.model.Franchise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FranchiseServiceImplTest {

    private TaskRepository taskRepository;
    private CategoryRepository categoryRepository;
    private TaskServiceImpl taskService;

    private final Category category = Category.builder().id("cat-1").name("Work").build();

    @BeforeEach
    void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);
        categoryRepository = Mockito.mock(CategoryRepository.class);
        taskService = new TaskServiceImpl(taskRepository, categoryRepository);
    }

    @Test
    void create_shouldReturnTaskResponse_whenCategoryExists() {
        TaskRequest request = new TaskRequest("Test Franchise", "Description", false, "cat-1");

        Franchise savedFranchise = new Franchise();
        savedFranchise.setId("task-1");
        savedFranchise.setTitle("Test Franchise");
        savedFranchise.setDescription("Description");
        savedFranchise.setCompleted(false);
        savedFranchise.setCategory(category);

        when(categoryRepository.findById("cat-1")).thenReturn(Mono.just(category));
        when(taskRepository.save(any(Franchise.class))).thenReturn(Mono.just(savedFranchise));

        StepVerifier.create(taskService.create(request))
                .expectNextMatches(response -> response.id().equals("task-1") &&
                        response.title().equals("Test Franchise") &&
                        response.category().id().equals("cat-1"))
                .verifyComplete();

        verify(categoryRepository).findById("cat-1");
        verify(taskRepository).save(any(Franchise.class));
    }

    @Test
    void create_shouldFail_whenCategoryDoesNotExist() {
        TaskRequest request = new TaskRequest("Test Franchise", "Description", false, "invalid-id");

        when(categoryRepository.findById("invalid-id")).thenReturn(Mono.empty());

        StepVerifier.create(taskService.create(request))
                .expectErrorMatches(error ->
                        error instanceof ResponseStatusException &&
                                ((ResponseStatusException) error).getStatusCode().value() == 400)
                .verify();

        verify(categoryRepository).findById("invalid-id");
        verify(taskRepository, never()).save(any());
    }

    @Test
    void update_shouldReturnUpdatedTask() {
        String taskId = "task-1";
        Franchise existingFranchise = new Franchise();
        existingFranchise.setId(taskId);
        existingFranchise.setTitle("Old Title");
        existingFranchise.setDescription("Old Description");
        existingFranchise.setCompleted(false);
        existingFranchise.setCategory(category);

        TaskRequest updateRequest = new TaskRequest("Updated", "New Desc", true, "cat-1");

        Franchise updatedFranchise = new Franchise();
        updatedFranchise.setId(taskId);
        updatedFranchise.setTitle("Updated");
        updatedFranchise.setDescription("New Desc");
        updatedFranchise.setCompleted(true);
        updatedFranchise.setCategory(category);

        when(taskRepository.findById(taskId)).thenReturn(Mono.just(existingFranchise));
        when(categoryRepository.findById("cat-1")).thenReturn(Mono.just(category));
        when(taskRepository.save(any(Franchise.class))).thenReturn(Mono.just(updatedFranchise));

        StepVerifier.create(taskService.update(taskId, updateRequest))
                .expectNextMatches(task -> task.id().equals(taskId) &&
                        task.title().equals("Updated") &&
                        task.completed())
                .verifyComplete();
    }

    @Test
    void update_shouldFail_whenTaskNotFound() {
        when(taskRepository.findById("notfound")).thenReturn(Mono.empty());

        TaskRequest request = new TaskRequest("Test", "Desc", true, "cat-1");

        when(categoryRepository.findById("cat-1")).thenReturn(Mono.just(category));

        StepVerifier.create(taskService.update("notfound", request))
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    void findById_shouldReturnTask() {
        Franchise franchise = new Franchise();
        franchise.setId("franchise-1");
        franchise.setTitle("Test");
        franchise.setCategory(category);

        when(taskRepository.findById("franchise-1")).thenReturn(Mono.just(franchise));

        StepVerifier.create(taskService.findById("franchise-1"))
                .expectNextMatches(t -> t.id().equals("franchise-1"))
                .verifyComplete();
    }

    @Test
    void findById_shouldFail_whenNotFound() {
        when(taskRepository.findById("notfound")).thenReturn(Mono.empty());

        StepVerifier.create(taskService.findById("notfound"))
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    void findAll_shouldReturnTasks() {
        Franchise franchise = new Franchise();
        franchise.setId("1");
        franchise.setTitle("Title");
        franchise.setCategory(category);

        when(taskRepository.findAll()).thenReturn(Flux.just(franchise));

        StepVerifier.create(taskService.findAll())
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void delete_shouldCallRepository() {
        when(taskRepository.deleteById("1")).thenReturn(Mono.empty());

        StepVerifier.create(taskService.delete("1"))
                .verifyComplete();

        verify(taskRepository).deleteById("1");
    }

    @Test
    void findByCategory_shouldReturnTasks() {
        Franchise franchise = new Franchise();
        franchise.setId("1");
        franchise.setCategory(category);

        when(categoryRepository.findById("cat-1")).thenReturn(Mono.just(category));
        when(taskRepository.findByCategory(category)).thenReturn(Flux.just(franchise));

        StepVerifier.create(taskService.findByCategory("cat-1"))
                .expectNextCount(1)
                .verifyComplete();
    }
}
