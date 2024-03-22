package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import ru.otus.hw.convertors.BookMapper;
import ru.otus.hw.convertors.BookMapperImpl;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

@WebFluxTest(controllers = BookController.class)

class BookControllerTest {

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @Autowired
    private WebTestClient webClient;

    @TestConfiguration
    static class BookMapperImplTestContextConfiguration {
        @Bean
        public BookMapper bookMapper() {
            return new BookMapperImpl();
        }
    }

    @Test
    void should_save_new_book_with_genre_and_author() {
        var bookDto = new BookCreateDto("Book title", 1L, 2L);
        Mockito.when(genreRepository.existsById(bookDto.getGenreId())).thenReturn(Mono.just(true));
        Mockito.when(authorRepository.existsById(bookDto.getAuthorId())).thenReturn(Mono.just(true));
        Mockito.when(bookRepository.save(Mockito.any(Book.class)))
                .thenReturn(Mono.just(new Book(100L, bookDto.getTitle(), bookDto.getAuthorId(), bookDto.getGenreId())));

        webClient.post()
                .uri("/api/v1/book")
                .body(BodyInserters.fromValue(bookDto))
                .exchange()
                .expectStatus().isCreated();
        Mockito.verify(genreRepository, Mockito.only()).existsById(bookDto.getGenreId());
        Mockito.verify(authorRepository, Mockito.only()).existsById(bookDto.getAuthorId());
        Mockito.verify(bookRepository, Mockito.only()).save(Mockito.any());
    }

    @Test
    void should_not_save_new_book_when_genre_absent() {
        var bookDto = new BookCreateDto("Book with not existed genre", 1L, 222L);

        Mockito.when(genreRepository.existsById(bookDto.getGenreId())).thenReturn(Mono.just(false));
        Mockito.when(authorRepository.existsById(bookDto.getAuthorId())).thenReturn(Mono.just(true));

        webClient.post()
                .uri("/api/v1/book")
                .body(BodyInserters.fromValue(bookDto))
                .exchange()
                .expectStatus().isNotFound();
        Mockito.verify(genreRepository, Mockito.atMostOnce()).existsById(bookDto.getGenreId());
        Mockito.verify(authorRepository, Mockito.atMostOnce()).existsById(bookDto.getAuthorId());
        Mockito.verify(bookRepository, Mockito.never()).save(Mockito.any(Book.class));
    }

    @Test
    void should_not_save_new_book_when_author_absent() {
        var bookDto = new BookCreateDto("Book with not existed author", 333L, 2L);

        Mockito.when(genreRepository.existsById(bookDto.getGenreId())).thenReturn(Mono.just(true));
        Mockito.when(authorRepository.existsById(bookDto.getAuthorId())).thenReturn(Mono.just(false));

        webClient.post()
                .uri("/api/v1/book")
                .body(BodyInserters.fromValue(bookDto))
                .exchange()
                .expectStatus().isNotFound();
        Mockito.verify(genreRepository, Mockito.atMostOnce()).existsById(bookDto.getGenreId());
        Mockito.verify(authorRepository, Mockito.atMostOnce()).existsById(bookDto.getAuthorId());
        Mockito.verify(bookRepository, Mockito.never()).save(Mockito.any(Book.class));
    }
}