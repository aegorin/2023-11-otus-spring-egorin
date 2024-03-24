package ru.otus.hw.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.BookAuthorGenreDto;
import ru.otus.hw.models.Book;

public interface BookRepository extends R2dbcRepository<Book, Long> {

    @Query("""
            SELECT b.id, b.title,
            a.id author_id, a.full_name author_name,
            g.id genre_id, g.name genre_name
            FROM books b
            JOIN authors a ON a.id = b.author_id
            JOIN genres g ON g.id = b.genre_id
            """)
    Flux<BookAuthorGenreDto> findAllBooksDto();
}
