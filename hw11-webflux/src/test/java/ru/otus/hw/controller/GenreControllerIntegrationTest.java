package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;
import ru.otus.hw.config.ConfigurationApp;
import ru.otus.hw.models.Genre;

import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(ConfigurationApp.class)
class GenreControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldReturnCorrectGenresList() {
        var webTestClientForTest = webTestClient.mutate()
                .responseTimeout(Duration.ofSeconds(5))
                .build();

        var result = webTestClientForTest
                .get().uri("/api/v1/genre")
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Genre.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<Genre> stepResult = null;
        for (long k = 1; k <= 3; k++) {
            String genreName = "Genre_" + k;
            Long genreId = k;
            stepResult = step.expectNextMatches(g -> genreId.equals(g.getId()) && genreName.equals(g.getName()));
        }
        stepResult.verifyComplete();
    }
}
