package ru.otus.hw.repositories;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.events.MongoBookCascadeDeleteEventsListener;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(MongoBookCascadeDeleteEventsListener.class)
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void shouldFindCommentById() {
        var actualComment = commentRepository.findById("1");
        var expected = mongoTemplate.findById("1", Comment.class);
        assertThat(actualComment)
                .isNotEmpty()
                .get()
                .usingRecursiveComparison(comparisonConfiguration())
                .isEqualTo(expected);
    }

    @Test
    void shouldReturnEmptyCommentListWhenBookAbsent() {
        var comments = commentRepository.findByBookId("_bad_id_");
        assertThat(comments).isEmpty();
    }

    @Test
    void shouldReturnCommentsByBookId() {
        var comment1 = mongoTemplate.findById("1", Comment.class);
        var comment2 = mongoTemplate.findById("2", Comment.class);
        var comments = commentRepository.findByBookId("1");

        assertThat(comments)
                .hasSize(2)
                .usingRecursiveFieldByFieldElementComparator(comparisonConfiguration())
                .containsExactlyInAnyOrder(comment1, comment2);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldSaveNewComment() {
        var book = mongoTemplate.findById("3", Book.class);
        var comment = new Comment(book, "new comment");

        var actual = commentRepository.save(comment);
        var expected = mongoTemplate.findById(actual.getId(), Comment.class);
        assertThat(actual)
                .isNotNull()
                .usingRecursiveComparison(comparisonConfiguration())
                .isEqualTo(expected);
    }

    @Test
    void shouldUpdateComment() {
        var comment = mongoTemplate.findById("1", Comment.class);
        comment.setText(java.util.UUID.randomUUID().toString());

        var actual = commentRepository.save(comment);

        var expected = mongoTemplate.findById(comment.getId(), Comment.class);
        assertThat(actual)
                .isNotNull()
                .usingRecursiveComparison(comparisonConfiguration())
                .isEqualTo(expected);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shoudDeleteCommentById() {
        var comment = mongoTemplate.findById("1", Comment.class);
        assertThat(comment).isNotNull();

        commentRepository.deleteById(comment.getId());

        assertThat(mongoTemplate.findById("1", Comment.class)).isNull();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteCommentsWhenDeleteBook() {
        var book = mongoTemplate.findById("1", Book.class);
        var comments = commentRepository.findByBookId(book.getId());
        assertThat(comments).isNotEmpty();

        bookRepository.deleteById(book.getId());
        assertThat(commentRepository.findByBookId(book.getId())).isEmpty();
    }

    private RecursiveComparisonConfiguration comparisonConfiguration() {
        Comparator<Book> bookComparator = Comparator.comparing(b -> String.valueOf(b.getId()));
        return RecursiveComparisonConfiguration.builder()
                .withComparatorForType(bookComparator, Book.class)
                .build();
    }
}