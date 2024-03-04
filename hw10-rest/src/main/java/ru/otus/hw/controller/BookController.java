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
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.services.BookService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;

    @GetMapping("/api/v1/book")
    public List<BookDto> allBooks() {
        return bookService.findAll();
    }

    @GetMapping("/api/v1/book/{bookId}")
    public BookUpdateDto editBook(@PathVariable long bookId) {
        var bookDto = bookService.findById(bookId);
        return new BookUpdateDto(bookDto.getId(),
                bookDto.getTitle(),
                bookDto.getAuthor().getId(),
                bookDto.getGenre().getId());
    }

    @DeleteMapping("/api/v1/book/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable long bookId) {
        bookService.deleteById(bookId);
    }

    @PostMapping("/api/v1/book")
    @ResponseStatus(HttpStatus.CREATED)
    public BookUpdateDto saveNewBook(@Valid @RequestBody BookCreateDto bookCreateDto) {
        return bookService.create(bookCreateDto);
    }

    @PutMapping("/api/v1/book/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBook(@Valid @RequestBody BookUpdateDto bookUpdateDto) {
        bookService.update(bookUpdateDto);
    }
}
