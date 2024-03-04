package ru.otus.hw.convertors;

import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

public interface BookMapper {

    Book toModel(BookCreateDto bookCreateDto, Author author, Genre genre);

    Book toModel(BookUpdateDto bookUpdateDto, Author author, Genre genre);

    BookDto toDto(Book book);

    BookUpdateDto toUpdateDto(Book book);
}
