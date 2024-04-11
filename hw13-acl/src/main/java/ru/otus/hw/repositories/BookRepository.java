package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.ListCrudRepository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends ListCrudRepository<Book, Long> {

    @Override
    @EntityGraph("books_with_author_and_genre")
    Optional<Book> findById(Long aLong);

    @Override
    @EntityGraph("books_with_author_and_genre")
    List<Book> findAll();

    boolean existsByIdAndOwnerId(Long bookId, Long userId);
}
