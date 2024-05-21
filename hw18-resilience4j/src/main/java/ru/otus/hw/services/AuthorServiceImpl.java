package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = "DatabaseBreaker", fallbackMethod = "findAllFallback")
    public List<AuthorDto> findAll() {
        return authorRepository.findAll().stream()
                .map(a -> new AuthorDto(a.getId(), a.getFullName()))
                .toList();
    }

    private List<AuthorDto> findAllFallback(Exception exception) {
        log.error(exception.getMessage(), exception);
        return List.of(new AuthorDto(-1L, "Unknown"));
    }
}
