package ru.otus.hw.services;

import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;

import java.util.List;

public interface BookService {
    BookDto findById(long id);

    List<BookDto> findAll();

    BookUpdateDto create(BookCreateDto bookCreateDto);

    BookUpdateDto update(BookUpdateDto bookUpdateDto);

    void deleteById(long id);
}
