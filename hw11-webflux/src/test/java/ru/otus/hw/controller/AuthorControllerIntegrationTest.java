package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;
import ru.otus.hw.config.ConfigurationApp;
import ru.otus.hw.models.Author;

import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(ConfigurationApp.class)
class AuthorControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldReturnCorrectAuthorsList() {
        var webTestClientForTest = webTestClient.mutate()
                .responseTimeout(Duration.ofSeconds(5))
                .build();

        var result = webTestClientForTest
                .get().uri("/api/v1/author")
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Author.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<Author> stepResult = null;
        for (long k = 1; k <= 3; k++) {
            String authorName = "Author_" + k;
            Long authorId = k;
            stepResult = step.expectNextMatches(a -> authorId.equals(a.getId()) && authorName.equals(a.getFullName()));
        }
        stepResult.verifyComplete();
    }
}
