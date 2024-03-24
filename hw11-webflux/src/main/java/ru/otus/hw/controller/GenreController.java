package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@RestController
public class GenreController {

    private final GenreRepository genreRepository;

    @GetMapping("/api/v1/genre")
    public Flux<Genre> allGenres() {
        return genreRepository.findAll();
    }
}
