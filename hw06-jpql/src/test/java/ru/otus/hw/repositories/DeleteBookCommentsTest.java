package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(value = {JpaBookCommentRepository.class, JpaBookRepository.class})
public class DeleteBookCommentsTest {

    @Autowired
    private JpaBookCommentRepository commentRepository;

    @Autowired
    private JpaBookRepository bookRepository;

    @Autowired
    private TestEntityManager em;

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
