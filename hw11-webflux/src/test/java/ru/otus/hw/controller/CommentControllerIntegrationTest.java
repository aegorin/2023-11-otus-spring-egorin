package ru.otus.hw.controller;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw.config.ConfigurationApp;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentUpdateDto;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(ConfigurationApp.class)
class CommentControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void returnAllCommentsForBook1() {
        webTestClient
                .get().uri("/api/v1/book/{bookId}/comment", 1)
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(CommentUpdateDto.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .expectNextMatches(dto -> "Comment_1_book_1".equals(dto.getText()))
                .expectNextMatches(dto -> "Comment_2_book_1".equals(dto.getText()))
                .expectComplete()
                .verify();
    }

    @Test
    void returnAllCommentsForBook2() {
        webTestClient
                .get().uri("/api/v1/book/{bookId}/comment", 2)
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(CommentUpdateDto.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .expectNextMatches(dto -> "Comment_1_book_2".equals(dto.getText()))
                .expectComplete()
                .verify();
    }

    @Test
    void returnEmptyCommentsForBook3() {
        webTestClient
                .get().uri("/api/v1/book/{bookId}/comment", 3)
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .isEmpty();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteCommentById() {
        webTestClient
                .delete().uri("/api/v1/comment/{commentId}", 3)
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .isEmpty();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void updateComment() {
        var result = webTestClient
                .get().uri("/api/v1/book/{bookId}/comment", 2)
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(CommentUpdateDto.class)
                .getResponseBody();

        List<CommentUpdateDto> commentUpdateDtoList = new ArrayList<>();
        StepVerifier.create(result)
                .assertNext(commentDto -> {
                    Assertions.assertNotNull(commentDto.getId());
                    Assertions.assertNotNull(commentDto.getText());
                    commentUpdateDtoList.add(commentDto);
                })
                .expectComplete()
                .verify();

        CommentUpdateDto dtoForChange = commentUpdateDtoList.get(0);
        String newText = "New text comment 1";
        webTestClient
                .put().uri("/api/v1/comment/{commentId}", dtoForChange.getId())
                .body(Mono.just(new CommentUpdateDto(dtoForChange.getId(), newText)), CommentUpdateDto.class)
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .isEmpty();
    }

    @Test
    void error_update_comment_when_text_is_null() {
        var commentWithoutText = new CommentUpdateDto(1L, null);
        webTestClient
                .put().uri("/api/v1/comment/{commentId}", 1)
                .body(Mono.just(commentWithoutText), commentWithoutText.getClass())
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errors").exists()
                .jsonPath("$.errors").isNotEmpty()
                .jsonPath("$.errors[0].field").value(Is.is("text"))
                .jsonPath("$.errors[0].message").isNotEmpty();
    }

    @Test
    void error_update_comment_when_text_is_empty() {
        var commentWithoutText = new CommentUpdateDto(1L, "  ");
        webTestClient
                .put().uri("/api/v1/comment/{commentId}", 1)
                .body(Mono.just(commentWithoutText), commentWithoutText.getClass())
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errors").exists()
                .jsonPath("$.errors").isNotEmpty()
                .jsonPath("$.errors[0].field").value(Is.is("text"))
                .jsonPath("$.errors[0].message").isNotEmpty();
    }

    @Test
    void error_update_comment_when_id_is_null() {
        var commentWithoutText = new CommentUpdateDto(null, "comment without id");
        webTestClient
                .put().uri("/api/v1/comment/{commentId}", 1)
                .body(Mono.just(commentWithoutText), commentWithoutText.getClass())
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errors").exists()
                .jsonPath("$.errors").isNotEmpty()
                .jsonPath("$.errors[0].field").value(Is.is("id"))
                .jsonPath("$.errors[0].message").isNotEmpty();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void insertNewCommentForBook() {
        Long bookId = 3L;
        webTestClient
                .get().uri("/api/v1/book/{bookId}/comment", bookId)
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .isEmpty();

        String commentText = "Comment text for book " + bookId;
        webTestClient
                .post().uri("/api/v1/comment")
                .body(Mono.just(new CommentCreateDto(commentText, bookId)), CommentCreateDto.class)
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isCreated()
                .returnResult(CommentUpdateDto.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(dto -> {
                    Assertions.assertNotNull(dto.getId());
                    Assertions.assertEquals(commentText, dto.getText());
                })
                .expectComplete()
                .verify();

        webTestClient
                .get().uri("/api/v1/book/{bookId}/comment", bookId)
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(CommentUpdateDto.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(dto -> {
                    Assertions.assertNotNull(dto.getId());
                    Assertions.assertEquals(commentText, dto.getText());
                })
                .expectComplete()
                .verify();
    }

    @Test
    void error_create_comment_with_empty_text() {
        var commentWithoutText = new CommentCreateDto("", 3L);
        webTestClient
                .post().uri("/api/v1/comment")
                .body(Mono.just(commentWithoutText), commentWithoutText.getClass())
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errors").exists()
                .jsonPath("$.errors").isNotEmpty()
                .jsonPath("$.errors[0].field").value(Is.is("text"))
                .jsonPath("$.errors[0].message").isNotEmpty();
    }

    @Test
    void error_create_comment_without_book_id() {
        var commentWithoutText = new CommentCreateDto("comment text test", null);
        webTestClient
                .post().uri("/api/v1/comment")
                .body(Mono.just(commentWithoutText), commentWithoutText.getClass())
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errors").exists()
                .jsonPath("$.errors").isNotEmpty()
                .jsonPath("$.errors[0].field").value(Is.is("bookId"))
                .jsonPath("$.errors[0].message").isNotEmpty();
    }
}
