package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcGenreRepository.class)
class JdbcGenreRepositoryTest {
    @Autowired
    private GenreRepository genreRepository;

    @Test
    void returnGenreById() {
        assertThat(genreRepository.findById(1L))
                .isNotEmpty()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(new Genre(1L, "Genre_1"));
    }

    @Test
    void loadAllGenre() {
        var genres = IntStream.range(1, 4).mapToObj(i -> new Genre(i, "Genre_" + i)).toList();
        assertThat(genreRepository.findAll())
                .containsExactlyElementsOf(genres);
    }
}
