package ru.otus.hw.convertors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

@Component
@RequiredArgsConstructor
public class BookMapperImpl implements BookMapper {

    @Override
    public Book toModel(BookCreateDto bookCreateDto, Author author, Genre genre) {
        var book = new Book();
        book.setTitle(bookCreateDto.getTitle());
        book.setAuthor(author);
        book.setGenre(genre);
        return book;
    }

    @Override
    public Book toModel(BookUpdateDto bookUpdateDto, Author author, Genre genre) {
        var book = new Book();
        book.setId(bookUpdateDto.getId());
        book.setTitle(bookUpdateDto.getTitle());
        book.setAuthor(author);
        book.setGenre(genre);
        return book;
    }

    @Override
    public BookDto toDto(Book book) {
        var author = book.getAuthor();
        var genre = book.getGenre();
        return new BookDto(book.getId(), book.getTitle(),
                new AuthorDto(author.getId(), author.getFullName()),
                new GenreDto(genre.getId(), genre.getName()));
    }

    @Override
    public BookUpdateDto toUpdateDto(Book book) {
        return new BookUpdateDto(book.getId(), book.getTitle(),
                book.getAuthor().getId(),
                book.getGenre().getId());
    }
}
