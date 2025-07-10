package com.accenture.to_do_List_backend.domain.repository;

import com.accenture.to_do_List_backend.domain.model.Category;
import com.accenture.to_do_List_backend.domain.model.Task;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface TaskRepository extends ReactiveMongoRepository<Task, String> {

    Flux<Task> findByCompleted(boolean completed);
    Flux<Task> findByCategory(Category category); // Filtro por ID de categoría
}