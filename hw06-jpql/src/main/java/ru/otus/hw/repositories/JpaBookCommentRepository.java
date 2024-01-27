package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.BookComment;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaBookCommentRepository implements BookCommentRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public JpaBookCommentRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<BookComment> findById(long commentId) {
        return Optional.ofNullable(entityManager.find(BookComment.class, commentId));
    }

    @Override
    public List<BookComment> findByBookId(long bookId) {
        return entityManager.createQuery(
                "FROM BookComment c " +
                "WHERE c.book.id = :book_id", BookComment.class)
                .setParameter("book_id", bookId)
                .getResultList();
    }

    @Override
    public BookComment save(BookComment bookComment) {
        if (bookComment.getId() > 0) {
            entityManager.persist(bookComment);
            return bookComment;
        }
        try {
            entityManager.getReference(BookComment.class, bookComment.getId());
            return entityManager.merge(bookComment);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            throw new EntityNotFoundException("no rows updated");
        }
    }

    @Override
    public void deleteById(long commentId) {
        try {
            var comment = entityManager.getReference(BookComment.class, commentId);
            entityManager.remove(comment);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            throw new EntityNotFoundException("no rows updated");
        }
    }
}
