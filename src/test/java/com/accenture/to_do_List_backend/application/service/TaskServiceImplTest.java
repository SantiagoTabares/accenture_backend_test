package com.accenture.to_do_List_backend.application.service;

import com.accenture.to_do_List_backend.application.dto.request.TaskRequest;

import com.accenture.to_do_List_backend.domain.model.Category;
import com.accenture.to_do_List_backend.domain.model.Task;
import com.accenture.to_do_List_backend.domain.repository.CategoryRepository;
import com.accenture.to_do_List_backend.domain.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

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
        TaskRequest request = new TaskRequest("Test Task", "Description", false, "cat-1");

        Task savedTask = new Task();
        savedTask.setId("task-1");
        savedTask.setTitle("Test Task");
        savedTask.setDescription("Description");
        savedTask.setCompleted(false);
        savedTask.setCategory(category);

        when(categoryRepository.findById("cat-1")).thenReturn(Mono.just(category));
        when(taskRepository.save(any(Task.class))).thenReturn(Mono.just(savedTask));

        StepVerifier.create(taskService.create(request))
                .expectNextMatches(response -> response.id().equals("task-1") &&
                        response.title().equals("Test Task") &&
                        response.category().id().equals("cat-1"))
                .verifyComplete();

        verify(categoryRepository).findById("cat-1");
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void create_shouldFail_whenCategoryDoesNotExist() {
        TaskRequest request = new TaskRequest("Test Task", "Description", false, "invalid-id");

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
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Old Title");
        existingTask.setDescription("Old Description");
        existingTask.setCompleted(false);
        existingTask.setCategory(category);

        TaskRequest updateRequest = new TaskRequest("Updated", "New Desc", true, "cat-1");

        Task updatedTask = new Task();
        updatedTask.setId(taskId);
        updatedTask.setTitle("Updated");
        updatedTask.setDescription("New Desc");
        updatedTask.setCompleted(true);
        updatedTask.setCategory(category);

        when(taskRepository.findById(taskId)).thenReturn(Mono.just(existingTask));
        when(categoryRepository.findById("cat-1")).thenReturn(Mono.just(category));
        when(taskRepository.save(any(Task.class))).thenReturn(Mono.just(updatedTask));

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
        Task task = new Task();
        task.setId("task-1");
        task.setTitle("Test");
        task.setCategory(category);

        when(taskRepository.findById("task-1")).thenReturn(Mono.just(task));

        StepVerifier.create(taskService.findById("task-1"))
                .expectNextMatches(t -> t.id().equals("task-1"))
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
        Task task = new Task();
        task.setId("1");
        task.setTitle("Title");
        task.setCategory(category);

        when(taskRepository.findAll()).thenReturn(Flux.just(task));

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
        Task task = new Task();
        task.setId("1");
        task.setCategory(category);

        when(categoryRepository.findById("cat-1")).thenReturn(Mono.just(category));
        when(taskRepository.findByCategory(category)).thenReturn(Flux.just(task));

        StepVerifier.create(taskService.findByCategory("cat-1"))
                .expectNextCount(1)
                .verifyComplete();
    }
}
