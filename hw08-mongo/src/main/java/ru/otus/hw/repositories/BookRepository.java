package ru.otus.hw.repositories;

import org.springframework.data.repository.ListCrudRepository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends ListCrudRepository<Book, String> {

    @Override
    Optional<Book> findById(String bookId);

    @Override
    List<Book> findAll();
}
