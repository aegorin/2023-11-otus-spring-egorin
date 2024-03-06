package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;

public interface BookService {

    Mono<BookUpdateDto> findById(Long bookId);

    Flux<BookDto> findAllBooksDto();

    Mono<BookUpdateDto> create(BookCreateDto bookCreateDto);

    Mono<Void> update(BookUpdateDto bookUpdateDto);

    Mono<Void> deleteById(Long bookId);
}
