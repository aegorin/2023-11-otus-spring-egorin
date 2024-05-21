package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;

    @Test
    void correctCommentsForBook() {
        var comment = new Comment(new Book(), "Book comment");
        comment.setId(20L);
        when(commentRepository.findByBookId(1L)).thenReturn(List.of(comment));

        var comments = commentService.findByBookId(1L);
        assertThat(comments).hasSize(1)
                .element(0)
                .matches(dto -> comment.getId() == dto.getId())
                .matches(dto -> comment.getText().equals(dto.getText()));
    }

    @Test
    void defaultCommentsForBook() {
        doThrow(RuntimeException.class).when(commentRepository).findByBookId(1L);

        var comments = commentService.findByBookId(1L);
        assertThat(comments).hasSize(1)
                .element(0)
                .matches(dto -> -1L == dto.getId())
                .matches(dto -> "Comment blank".equals(dto.getText()));
    }
}
