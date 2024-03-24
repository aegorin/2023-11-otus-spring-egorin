package ru.otus.hw.convertors;

import ru.otus.hw.dto.BookAuthorGenreDto;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.models.Book;

public interface BookMapper {

    Book toModel(BookCreateDto dtoCreateBook);

    Book toModel(BookUpdateDto dtoUpdateBook);

    BookDto toBookDto(BookAuthorGenreDto bookAuthorGenreDto);

    BookUpdateDto toUpdateDto(Book book);
}
