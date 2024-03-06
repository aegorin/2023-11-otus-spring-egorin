package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Genre;

public interface GenreService {

    Mono<Genre> getById(Long genreId);

    Flux<Genre> findAll();
}
