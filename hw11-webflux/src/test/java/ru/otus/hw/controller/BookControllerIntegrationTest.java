package ru.otus.hw.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw.config.ConfigurationApp;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.GenreDto;

import java.util.concurrent.atomic.AtomicLong;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(ConfigurationApp.class)
class BookControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void create_update_delete_book_test() {
        String bookTitle = "Title book for test";
        Long authorId = 2L;
        Long genreId = 3L;
        AtomicLong bookId = new AtomicLong();

        webTestClient
                .post().uri("/api/v1/book")
                .body(Mono.just(new BookCreateDto(bookTitle, authorId, genreId)), BookCreateDto.class)
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isCreated()
                .returnResult(BookUpdateDto.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(bookDto -> {
                    Assertions.assertNotNull(bookDto.getId());
                    Assertions.assertEquals(bookTitle, bookDto.getTitle());
                    Assertions.assertEquals(authorId, bookDto.getAuthorId());
                    Assertions.assertEquals(genreId, bookDto.getGenreId());
                    bookId.set(bookDto.getId());
                })
                .expectComplete()
                .verify();

        webTestClient
                .get().uri("/api/v1/book/{bookId}", bookId.get())
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookUpdateDto.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .expectNextMatches(dto -> bookTitle.equals(dto.getTitle()))
                .expectComplete()
                .verify();

        String changedBookTitle = "Title book for test with changes";
        webTestClient
                .put().uri("/api/v1/book/{bookId}", bookId.get())
                .body(Mono.just(new BookUpdateDto(bookId.get(), changedBookTitle, authorId, genreId)), BookUpdateDto.class)
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .isEmpty();

        webTestClient
                .get().uri("/api/v1/book/{bookId}", bookId.get())
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookUpdateDto.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .expectNextMatches(dto -> changedBookTitle.equals(dto.getTitle()))
                .expectComplete()
                .verify();

        webTestClient
                .delete().uri("/api/v1/book/{bookId}", bookId.get())
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .isEmpty();

        webTestClient
                .get().uri("/api/v1/book/{bookId}", bookId.get())
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void findAllBooks() {
        var book1 = new BookDto(1L, "Book_1", new AuthorDto(1L, "Author_1"), new GenreDto(1L, "Genre_1"));
        var book2 = new BookDto(2L, "Book_2", new AuthorDto(2L, "Author_2"), new GenreDto(2L, "Genre_2"));
        var book3 = new BookDto(3L, "Book_3", new AuthorDto(3L, "Author_3"), new GenreDto(3L, "Genre_3"));

        webTestClient
                .get().uri("/api/v1/book")
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .expectNextMatches(book1::equals)
                .expectNextMatches(book2::equals)
                .expectNextMatches(book3::equals)
                .expectComplete()
                .verify();
    }

    @Test
    void should_return_correct_book_by_id() {
        var bookUpdateDto = new BookUpdateDto(1L, "Book_1", 1L, 1L);
        webTestClient
                .get().uri("/api/v1/book/{bookId}", bookUpdateDto.getId())
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookUpdateDto.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .expectNextMatches(bookUpdateDto::equals)
                .expectComplete()
                .verify();
    }

    @Test
    void should_response_500_when_bookId_not_digits() {
        webTestClient
                .get().uri("/api/v1/book/{bookId}", "not-valid-book-id")
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.errors").isNotEmpty();
    }

    @Test
    void should_response_404_when_book_not_exists() {
        webTestClient
                .get().uri("/api/v1/book/{bookId}", 98765L)
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errors").isNotEmpty();
    }

    @Test
    void should_not_create_book_with_blank_title() {
        var bookCreateDto = new BookCreateDto(" ", 33L, 22L);
        webTestClient
                .post().uri("/api/v1/book")
                .body(Mono.just(bookCreateDto), bookCreateDto.getClass())
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errors").isNotEmpty()
                .jsonPath("$.errors[0].field").hasJsonPath()
                .jsonPath("$.errors[0].field").isEqualTo("title")
                .jsonPath("$.errors[0].message").isNotEmpty();
    }

    @Test
    void should_not_create_book_with_one_char_title() {
        var bookCreateDto = new BookCreateDto("r", 33L, 22L);
        webTestClient
                .post().uri("/api/v1/book")
                .body(Mono.just(bookCreateDto), bookCreateDto.getClass())
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errors").isNotEmpty()
                .jsonPath("$.errors[0].field").hasJsonPath()
                .jsonPath("$.errors[0].field").isEqualTo("title")
                .jsonPath("$.errors[0].message").isNotEmpty();
    }

    @Test
    void should_not_create_book_when_empty_author() {
        var bookCreateDto = new BookCreateDto("Test book without author", null, 22L);
        webTestClient
                .post().uri("/api/v1/book")
                .body(Mono.just(bookCreateDto), bookCreateDto.getClass())
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errors").isNotEmpty()
                .jsonPath("$.errors[0].field").hasJsonPath()
                .jsonPath("$.errors[0].field").isEqualTo("authorId")
                .jsonPath("$.errors[0].message").isNotEmpty();
    }

    @Test
    void should_not_create_book_when_empty_genre() {
        var bookCreateDto = new BookCreateDto("Test book without genre", 333L, null);
        webTestClient
                .post().uri("/api/v1/book")
                .body(Mono.just(bookCreateDto), bookCreateDto.getClass())
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errors").isNotEmpty()
                .jsonPath("$.errors[0].field").hasJsonPath()
                .jsonPath("$.errors[0].field").isEqualTo("genreId")
                .jsonPath("$.errors[0].message").isNotEmpty();
    }

    @Test
    void should_not_update_book_with_one_char_title() {
        var bookUpdateDto = new BookUpdateDto(1L, "F", 2L, 3L);
        webTestClient
                .put().uri("/api/v1/book/{bookId}", bookUpdateDto.getId())
                .body(Mono.just(bookUpdateDto), bookUpdateDto.getClass())
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errors[0].field").hasJsonPath()
                .jsonPath("$.errors[0].field").isEqualTo("title")
                .jsonPath("$.errors[0].message").isNotEmpty();
    }

    @Test
    void should_not_update_book_when_author_empty() {
        var bookUpdateDto = new BookUpdateDto(1L, "Book without author", null, 3L);
        webTestClient
                .put().uri("/api/v1/book/{bookId}", bookUpdateDto.getId())
                .body(Mono.just(bookUpdateDto), bookUpdateDto.getClass())
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errors[0].field").hasJsonPath()
                .jsonPath("$.errors[0].field").isEqualTo("authorId")
                .jsonPath("$.errors[0].message").isNotEmpty();
    }

    @Test
    void should_not_update_book_when_genre_empty() {
        var bookUpdateDto = new BookUpdateDto(1L, "Book without genre", 13L, null);
        webTestClient
                .put().uri("/api/v1/book/{bookId}", bookUpdateDto.getId())
                .body(Mono.just(bookUpdateDto), bookUpdateDto.getClass())
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errors[0].field").hasJsonPath()
                .jsonPath("$.errors[0].field").isEqualTo("genreId")
                .jsonPath("$.errors[0].message").isNotEmpty();
    }
}
