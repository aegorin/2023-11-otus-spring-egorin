package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.convertors.BookMapper;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.repositories.BookRepository;

@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @GetMapping("/api/v1/book")
    public Flux<BookDto> allBooks() {
        return bookRepository.findAllBooksDto()
                .map(bookMapper::toBookDto);
    }

    @GetMapping("/api/v1/book/{bookId}")
    public Mono<BookUpdateDto> getBookById(@PathVariable long bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toUpdateDto)
                .switchIfEmpty(Mono.error(
                        new NotFoundException("bookId", "Book with id %d not found".formatted(bookId))));
    }

    @PostMapping("/api/v1/book")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BookUpdateDto> createNewBook(@Valid @RequestBody BookCreateDto bookCreateDto) {
        return Mono.just(bookMapper.toModel(bookCreateDto))
                .flatMap(bookRepository::save)
                .map(bookMapper::toUpdateDto);
    }

    @PutMapping("/api/v1/book/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> updateBook(@Valid @RequestBody BookUpdateDto bookUpdateDto) {
        return Mono.just(bookMapper.toModel(bookUpdateDto))
                .flatMap(bookRepository::save)
                .flatMap(b -> Mono.empty());
    }

    @DeleteMapping("/api/v1/book/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBook(@PathVariable long bookId) {
        return bookRepository.deleteById(bookId);
    }
}
