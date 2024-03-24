package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentUpdateDto;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

@WebFluxTest(controllers = CommentController.class)
class CommentControllerTest {

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private WebTestClient webClient;

    @Test
    void should_return_404_when_book_not_found() {
        long bookId = 404L;
        Mockito.when(bookRepository.existsById(bookId)).thenReturn(Mono.just(false));
        Mockito.when(commentRepository.save(Mockito.any())).thenReturn(Mono.just(new Comment("Test comment", bookId)));

        webClient.post()
                .uri("/api/v1/comment")
                .body(BodyInserters.fromValue(new CommentCreateDto("Test comment", bookId)))
                .exchange()
                .expectStatus().isNotFound();
        Mockito.verify(bookRepository, Mockito.times(1)).existsById(bookId);
        Mockito.verify(commentRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void should_create_new_comment() {
        long bookId = 1L;
        Mockito.when(bookRepository.existsById(bookId)).thenReturn(Mono.just(true));
        Mockito.when(commentRepository.save(Mockito.any())).thenReturn(Mono.just(new Comment("comment", bookId)));

        webClient.post()
                .uri("/api/v1/comment")
                .body(BodyInserters.fromValue(new CommentCreateDto("Test comment", bookId)))
                .exchange()
                .expectStatus().isCreated();
        Mockito.verify(bookRepository, Mockito.times(1)).existsById(bookId);
        Mockito.verify(commentRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void should_return_404_when_comment_not_found() {
        var comment = new Comment("not existed comment", 1L);
        comment.setId(444L);

        Mockito.when(commentRepository.findById(comment.getId())).thenReturn(Mono.empty());
        Mockito.when(commentRepository.save(Mockito.any())).thenReturn(Mono.just(comment));

        webClient.put()
                .uri("/api/v1/comment/{commentId}", comment.getId())
                .body(BodyInserters.fromValue(new CommentUpdateDto(comment.getId(), comment.getText())))
                .exchange()
                .expectStatus().isNotFound();
        Mockito.verify(commentRepository, Mockito.times(1)).findById(comment.getId());
        Mockito.verify(commentRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void should_update_comment() {
        var comment = new Comment("existed comment", 1L);
        comment.setId(111L);

        Mockito.when(commentRepository.findById(comment.getId()))
                .thenReturn(Mono.just(comment));
        Mockito.when(commentRepository.save(Mockito.any())).thenReturn(Mono.just(comment));

        webClient.put()
                .uri("/api/v1/comment/{commentId}", comment.getId())
                .body(BodyInserters.fromValue(new CommentUpdateDto(comment.getId(), comment.getText())))
                .exchange()
                .expectStatus().isNoContent();
        Mockito.verify(commentRepository, Mockito.times(1)).findById(comment.getId());
        Mockito.verify(commentRepository, Mockito.times(1)).save(Mockito.any());
    }
}
