package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaCommentRepository implements CommentRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public JpaCommentRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Comment> findById(long commentId) {
        return Optional.ofNullable(entityManager.find(Comment.class, commentId));
    }

    @Override
    public List<Comment> findByBookId(long bookId) {
        return entityManager.createQuery(
                "FROM Comment c " +
                "WHERE c.book.id = :book_id", Comment.class)
                .setParameter("book_id", bookId)
                .getResultList();
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == 0) {
            entityManager.persist(comment);
            return comment;
        }
        return entityManager.merge(comment);
    }

    @Override
    public void deleteById(long commentId) {
        var comment = entityManager.find(Comment.class, commentId);
        if (comment == null) {
            throw new EntityNotFoundException("no rows updated");
        }
        entityManager.remove(comment);
    }
}
