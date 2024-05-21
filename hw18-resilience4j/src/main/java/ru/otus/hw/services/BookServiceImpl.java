package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.controller.NotFoundException;
import ru.otus.hw.convertors.BookMapper;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Override
    @Transactional(readOnly = true)
    public BookDto findById(long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = "DatabaseBreaker", fallbackMethod = "findAllFallback")
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public BookUpdateDto create(BookCreateDto bookCreateDto) {
        var book = bookMapper.toModel(bookCreateDto);
        book = bookRepository.save(book);
        return bookMapper.toUpdateDto(book);
    }

    @Override
    @Transactional
    public BookUpdateDto update(BookUpdateDto bookUpdateDto) {
        if (!bookRepository.existsById(bookUpdateDto.getId())) {
            throw new NotFoundException("Book with id %d not found".formatted(bookUpdateDto.getId()));
        }
        var book = bookMapper.toModel(bookUpdateDto);
        book = bookRepository.save(book);
        return bookMapper.toUpdateDto(book);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private List<BookDto> findAllFallback(Exception exception) {
        log.error(exception.getMessage(), exception);
        return List.of(defaultBookDto());
    }

    private BookDto defaultBookDto() {
        return new BookDto(-1L, "Unknow book",
                new AuthorDto(-1L, "Unknown"),
                new GenreDto(-1L, "Unknown"));
    }
}
