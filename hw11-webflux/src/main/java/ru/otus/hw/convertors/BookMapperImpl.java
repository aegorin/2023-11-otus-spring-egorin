package ru.otus.hw.convertors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookAuthorGenreDto;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Book;

@Component
@RequiredArgsConstructor
public class BookMapperImpl implements BookMapper {

    @Override
    public Book toModel(BookCreateDto dtoCreateBook) {
        var book = new Book();
        book.setTitle(dtoCreateBook.getTitle());
        book.setAuthorId(dtoCreateBook.getAuthorId());
        book.setGenreId(dtoCreateBook.getGenreId());
        return book;
    }

    @Override
    public Book toModel(BookUpdateDto dtoUpdateBook) {
        return new Book(
                dtoUpdateBook.getId(),
                dtoUpdateBook.getTitle(),
                dtoUpdateBook.getAuthorId(),
                dtoUpdateBook.getGenreId());
    }

    @Override
    public BookDto toBookDto(BookAuthorGenreDto bookAuthorGenreDto) {
        return new BookDto(bookAuthorGenreDto.id(), bookAuthorGenreDto.title(),
                new AuthorDto(bookAuthorGenreDto.authorId(), bookAuthorGenreDto.authorName()),
                new GenreDto(bookAuthorGenreDto.genreId(), bookAuthorGenreDto.genreName()));
    }

    @Override
    public BookUpdateDto toUpdateDto(Book book) {
        return new BookUpdateDto(book.getId(),
                book.getTitle(),
                book.getAuthorId(),
                book.getGenreId());
    }
}
