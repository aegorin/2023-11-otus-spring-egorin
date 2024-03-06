package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Author;

public interface AuthorService {

    Mono<Author> getById(Long authorId);

    Flux<Author> findAll();
}
