package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookRepositoryJdbc implements BookRepository {
    private final NamedParameterJdbcOperations namedJdbc;

    @Override
    public List<Book> findAll() {
        return namedJdbc.query("""
                SELECT b.id, b.title, b.author_id, b.genre_id,
                a.full_name AS author_name, g.name AS genre_name
                FROM books AS b
                JOIN authors AS a ON a.id = b.author_id
                JOIN genres AS g ON g.id = b.genre_id""", new BookRowMapper());
    }

    @Override
    public Optional<Book> findById(long id) {
        try {
            var book = namedJdbc.queryForObject("""
                SELECT b.id, b.title, b.author_id, b.genre_id,
                a.full_name AS author_name, g.name AS genre_name
                FROM books AS b
                JOIN authors AS a ON a.id = b.author_id
                JOIN genres AS g ON g.id = b.genre_id
                WHERE b.id = :book_id""", Map.of("book_id", id), new BookRowMapper());
            return Optional.ofNullable(book);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        namedJdbc.update("DELETE FROM books WHERE id = :id", Map.of("id", id));
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        var bookProperties = Map.of("title", book.getTitle(),
                "author", book.getAuthor().getId(),
                "genre", book.getGenre().getId());
        namedJdbc.update("INSERT INTO books (title, author_id, genre_id) VALUES(:title, :author, :genre)",
                new MapSqlParameterSource(bookProperties), keyHolder, new String[]{"id"});

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        int countUpdated = namedJdbc.update(
                "UPDATE books SET title = :title, author_id = :author, genre_id = :genre WHERE id = :id",
                Map.of("id", book.getId(), "title", book.getTitle(),
                        "author", book.getAuthor().getId(), "genre", book.getGenre().getId()));
        if (countUpdated == 0) {
            throw new EntityNotFoundException("no rows updated");
        }
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            var id = rs.getLong("id");
            var title = rs.getString("title");
            return new Book(id, title,
                    new Author(rs.getLong("author_id"), rs.getString("author_name")),
                    new Genre(rs.getLong("genre_id"), rs.getString("genre_name")));
        }
    }
}
