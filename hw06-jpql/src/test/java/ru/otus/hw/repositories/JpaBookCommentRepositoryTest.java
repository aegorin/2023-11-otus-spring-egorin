package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DataJpaTest
@Import(value = {JpaBookCommentRepository.class, JpaBookRepository.class})
class JpaBookCommentRepositoryTest {

    @Autowired
    private BookCommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    void shouldFindCommentById() {
        var actualComment = commentRepository.findById(1);
        var expected = em.find(BookComment.class, 1);
        assertThat(actualComment)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldReturnEmptyCommentListWhenBookAbsent() {
        var comments = commentRepository.findByBookId(Integer.MIN_VALUE);
        assertThat(comments).isEmpty();
    }

    @Test
    void shouldReturnCommentsByBookId() {
        var comment1 = em.find(BookComment.class, 1);
        var comment2 = em.find(BookComment.class, 2);
        var comments = commentRepository.findByBookId(1);

        assertThat(comments).containsExactlyInAnyOrder(comment1, comment2);
    }

    @Test
    void shouldSaveNewComment() {
        var book = em.find(Book.class, 1);
        var comment = new BookComment(book);
        comment.setComment("new comment");

        var actual = commentRepository.save(comment);
        var expected = em.find(BookComment.class, actual.getId());
        assertThat(actual)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldUpdateComment() {
        var comment = em.find(BookComment.class, 1);
        comment.setComment(java.util.UUID.randomUUID().toString());

        var actual = commentRepository.save(comment);
        em.flush();
        em.detach(actual);

        var expected = em.find(BookComment.class, comment.getId());
        assertThat(actual)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldExceptionWhenUpdateNotPersistedComment() {
        var notPersisted = new BookComment();
        notPersisted.setId(Integer.MIN_VALUE);

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> commentRepository.save(notPersisted));
    }

    @Test
    void shoudDeleteCommentById() {
        var comment = em.find(BookComment.class, 1);
        assertThat(comment).isNotNull();

        commentRepository.deleteById(comment.getId());

        assertThat(em.find(BookComment.class, 1)).isNull();
    }

    @Test
    void shoudExceptionWhenDeleteNotPersistedCommentById() {
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> commentRepository.deleteById(Integer.MIN_VALUE));
    }

    @Test
    void shouldDeleteCommentsWhenDeleteBook() {
        var book = em.find(Book.class, 1);
        var comments = commentRepository.findByBookId(book.getId());
        assertThat(comments).isNotEmpty();

        bookRepository.deleteById(book.getId());
        em.flush();

        assertThat(commentRepository.findByBookId(book.getId())).isEmpty();
    }
}