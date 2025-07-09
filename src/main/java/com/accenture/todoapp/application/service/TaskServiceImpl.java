package com.accenture.todoapp.application.service;

import com.accenture.todoapp.application.dto.request.TaskRequest;
import com.accenture.todoapp.application.dto.response.CategoryResponse;
import com.accenture.todoapp.application.dto.response.TaskResponse;

import com.accenture.todoapp.domain.model.Category;
import com.accenture.todoapp.domain.model.Task;
import com.accenture.todoapp.domain.repository.CategoryRepository;
import com.accenture.todoapp.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Mono<TaskResponse> create(TaskRequest request) {
        return categoryRepository.findById(request.categoryId())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoría no encontrada")))
                .flatMap(category -> {
                    Task task = new Task();
                    task.setTitle(request.title());
                    task.setDescription(request.description());
                    task.setCompleted(request.completed());
                    task.setCategory(category);
                    return taskRepository.save(task)
                            .map(this::toResponse);
                });
    }

    @Override
    public Mono<TaskResponse> update(String id, TaskRequest request) {
        if (request.categoryId() == null || request.categoryId().isBlank()) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID de categoría no puede ser nulo ni vacío"));
        }

        Mono<Task> taskMono = taskRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarea no encontrada")));

        Mono<Category> categoryMono = categoryRepository.findById(request.categoryId())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoría no encontrada")));

        return Mono.zip(taskMono, categoryMono)
                .flatMap(tuple -> {
                    Task task = tuple.getT1();
                    Category category = tuple.getT2();

                    task.setTitle(request.title());
                    task.setDescription(request.description());
                    task.setCompleted(request.completed());
                    task.setCategory(category); // 🔒 Ya no puede ser nulo

                    return taskRepository.save(task)
                            .map(this::toResponse);
                });
    }

    @Override
    public Mono<TaskResponse> findById(String id) {
        return taskRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarea no encontrada")))
                .map(this::toResponse);
    }
    @Override
    public Flux<TaskResponse> findAll() {
        return taskRepository.findAll()
                .map(this::toResponse);
    }

    @Override
    public Mono<Void> delete(String id) {
        return taskRepository.deleteById(id);
    }

    @Override
    public Flux<TaskResponse> findByCategory(String categoryId) {
        return categoryRepository.findById(categoryId)       // 🔍 Buscas la Category por su ID
                .flatMapMany(category ->                         // ✅ Si la encuentra...
                        taskRepository.findByCategory(category)      // 🔍 ...buscas tareas asociadas a esa Category
                                .map(this::toResponse)
                );
    }

    private TaskResponse toResponse(Task task) {
        Category category = task.getCategory();
        CategoryResponse categoryResponse = new CategoryResponse(
                category.getId(),
                category.getName()
        );

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted(),
                categoryResponse
        );
    }
}
