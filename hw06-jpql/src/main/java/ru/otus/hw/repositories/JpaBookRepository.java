package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Book> findAll() {
        return entityManager.createQuery(
                "SELECT b " +
                "FROM Book b " +
                "JOIN FETCH b.genre " +
                "JOIN FETCH b.author", Book.class)
                .getResultList();
    }

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(entityManager.find(Book.class, id));
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            entityManager.persist(book);
            return book;
        }
        try {
            entityManager.getReference(Book.class, book.getId());
            return entityManager.merge(book);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            throw new EntityNotFoundException("no rows updated");
        }
    }

    @Override
    public void deleteById(long id) {
        try {
            var book = entityManager.find(Book.class, id);
            entityManager.remove(book);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            throw new EntityNotFoundException("no rows updated");
        }
    }
}
