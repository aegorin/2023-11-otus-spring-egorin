package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public Book create(String title, String authorId, String genreId) {
        var book = new Book();
        book.setTitle(title);
        book.setAuthor(authorById(authorId));
        book.setGenre(genreById(genreId));
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book update(String id, String title, String authorId, String genreId) {
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));
        book.setTitle(title);
        book.setAuthor(authorById(authorId));
        book.setGenre(genreById(genreId));
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }

    private Author authorById(String authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));
    }

    private Genre genreById(String genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %s not found".formatted(genreId)));
    }
}
