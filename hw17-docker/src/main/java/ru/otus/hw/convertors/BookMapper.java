package ru.otus.hw.convertors;

import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.models.Book;

public interface BookMapper {

    Book toModel(BookCreateDto bookCreateDto);

    Book toModel(BookUpdateDto bookUpdateDto);

    BookDto toDto(Book book);

    BookUpdateDto toUpdateDto(Book book);
}
