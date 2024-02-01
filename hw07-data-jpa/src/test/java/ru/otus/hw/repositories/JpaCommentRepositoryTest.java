package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(value = {JpaBookRepository.class})
class JpaCommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    void shouldFindCommentById() {
        var actualComment = commentRepository.findById(1L);
        var expected = em.find(Comment.class, 1);
        assertThat(actualComment)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldReturnEmptyCommentListWhenBookAbsent() {
        var comments = commentRepository.findByBookId(Long.MIN_VALUE);
        assertThat(comments).isEmpty();
    }

    @Test
    void shouldReturnCommentsByBookId() {
        var comment1 = em.find(Comment.class, 1);
        var comment2 = em.find(Comment.class, 2);
        var comments = commentRepository.findByBookId(1L);

        assertThat(comments).containsExactlyInAnyOrder(comment1, comment2);
    }

    @Test
    void shouldSaveNewComment() {
        var book = em.find(Book.class, 1);
        var comment = new Comment(book);
        comment.setComment("new comment");

        var actual = commentRepository.save(comment);
        var expected = em.find(Comment.class, actual.getId());
        assertThat(actual)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldUpdateComment() {
        var comment = em.find(Comment.class, 1);
        comment.setComment(java.util.UUID.randomUUID().toString());

        var actual = commentRepository.save(comment);
        em.flush();
        em.detach(actual);

        var expected = em.find(Comment.class, comment.getId());
        assertThat(actual)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shoudDeleteCommentById() {
        var comment = em.find(Comment.class, 1);
        assertThat(comment).isNotNull();

        commentRepository.deleteById(comment.getId());

        assertThat(em.find(Comment.class, 1)).isNull();
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