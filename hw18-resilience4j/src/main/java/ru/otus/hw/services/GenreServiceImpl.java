package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = "DatabaseBreaker", fallbackMethod = "findAllFallBack")
    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream()
                .map(g -> new GenreDto(g.getId(), g.getName()))
                .toList();
    }

    private List<GenreDto> findAllFallBack(Exception exception) {
        log.error(exception.getMessage(), exception);
        return List.of(new GenreDto(-1L, "Unknown genre"));
    }
}
