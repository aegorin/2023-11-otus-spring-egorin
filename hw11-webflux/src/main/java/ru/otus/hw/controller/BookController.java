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
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookRepository bookRepository;

    private final GenreRepository genreRepository;

    private final AuthorRepository authorRepository;

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
        long genreId = bookCreateDto.getGenreId();
        long authorId = bookCreateDto.getAuthorId();
        return Mono.zip(genreRepository.existsById(genreId), authorRepository.existsById(authorId))
                .map(tuple -> {
                    if (!tuple.getT1()) {
                        throw new NotFoundException("genreId", "Genre with id %d not found".formatted(genreId));
                    }
                    if (!tuple.getT2()) {
                        throw new NotFoundException("authorId", "Author with id %d not found".formatted(authorId));
                    }
                    return bookMapper.toModel(bookCreateDto);
                })
                .onErrorStop()
                .flatMap(bookRepository::save)
                .map(bookMapper::toUpdateDto);
    }

    @PutMapping("/api/v1/book/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> updateBook(@Valid @RequestBody BookUpdateDto bookUpdateDto) {
        long bookId = bookUpdateDto.getId();
        long genreId = bookUpdateDto.getGenreId();
        long authorId = bookUpdateDto.getAuthorId();
        return Mono.zip(bookRepository.existsById(bookId),
                        genreRepository.existsById(genreId),
                        authorRepository.existsById(authorId))
                .map(tuple -> {
                    if (!tuple.getT1()) {
                        throw new NotFoundException("bookId", "Book with id %d not found".formatted(bookId));
                    }
                    if (!tuple.getT2()) {
                        throw new NotFoundException("genreId", "Genre with id %d not found".formatted(genreId));
                    }
                    if (!tuple.getT3()) {
                        throw new NotFoundException("authorId", "Author with id %d not found".formatted(authorId));
                    }
                    return bookMapper.toModel(bookUpdateDto);
                })
                .onErrorStop()
                .flatMap(bookRepository::save)
                .flatMap(b -> Mono.empty());
    }

    @DeleteMapping("/api/v1/book/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBook(@PathVariable long bookId) {
        return bookRepository.deleteById(bookId);
    }
}
