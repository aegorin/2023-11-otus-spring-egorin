package ru.otus.hw.convertors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.controller.NotFoundException;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.GenreRepository;

@Component
@RequiredArgsConstructor
public class BookMapperImpl implements BookMapper {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @Override
    public Book toModel(BookCreateDto bookCreateDto) {
        var book = new Book();
        book.setTitle(bookCreateDto.getTitle());
        book.setAuthor(authorById(bookCreateDto.getIdAuthor()));
        book.setGenre(genreById(bookCreateDto.getIdGenre()));
        return book;
    }

    @Override
    public Book toModel(BookUpdateDto bookUpdateDto) {
        var book = new Book();
        book.setId(bookUpdateDto.getId());
        book.setTitle(bookUpdateDto.getTitle());
        book.setAuthor(authorById(bookUpdateDto.getIdAuthor()));
        book.setGenre(genreById(bookUpdateDto.getIdGenre()));
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

    private Author authorById(Long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author with id %d not found".formatted(authorId)));
    }

    private Genre genreById(Long genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new NotFoundException("Genre with id %d not found".formatted(genreId)));
    }
}
