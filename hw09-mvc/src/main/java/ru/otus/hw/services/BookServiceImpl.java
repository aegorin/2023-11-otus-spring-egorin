package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public BookDto findById(long id) {
        return bookRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional
    public BookUpdateDto create(BookCreateDto bookCreateDto) {
        var book = new Book();
        book.setTitle(bookCreateDto.getTitle());
        book.setAuthor(authorById(bookCreateDto.getIdAuthor()));
        book.setGenre(genreById(bookCreateDto.getIdGenre()));
        book = bookRepository.save(book);
        return toUpdateDto(book);
    }

    @Override
    @Transactional
    public BookUpdateDto update(BookUpdateDto bookUpdateDto) {
        var book = bookRepository.findById(bookUpdateDto.getId())
                .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(bookUpdateDto.getId())));
        book.setTitle(bookUpdateDto.getTitle());
        book.setAuthor(authorById(bookUpdateDto.getIdAuthor()));
        book.setGenre(genreById(bookUpdateDto.getIdGenre()));
        book = bookRepository.save(book);
        return toUpdateDto(book);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private BookDto toDto(Book book) {
        var author = book.getAuthor();
        var genre = book.getGenre();
        return new BookDto(book.getId(), book.getTitle(),
                new AuthorDto(author.getId(), author.getFullName()),
                new GenreDto(genre.getId(), genre.getName()));
    }

    private BookUpdateDto toUpdateDto(Book book) {
        return new BookUpdateDto(book.getId(), book.getTitle(),
                book.getAuthor().getId(),
                book.getGenre().getId());
    }

    private Author authorById(long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author with id %d not found".formatted(authorId)));
    }

    private Genre genreById(long genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new NotFoundException("Genre with id %d not found".formatted(genreId)));
    }
}
