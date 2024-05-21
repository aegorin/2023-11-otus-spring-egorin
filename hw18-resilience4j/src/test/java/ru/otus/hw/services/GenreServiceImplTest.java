package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
class GenreServiceImplTest {

    @Autowired
    private GenreService genreService;

    @MockBean
    private GenreRepository genreRepository;

    @Test
    void findAll() {
        var genre = new Genre(10, "Correct genre");
        doReturn(List.of(genre)).when(genreRepository).findAll();

        var genresDto = genreService.findAll();
        assertThat(genresDto).hasSize(1)
                .element(0)
                .matches(dto -> genre.getId() == dto.getId())
                .matches(dto -> genre.getName().equals(dto.getName()));
    }

    @Test
    void findAllFallback() {
        doThrow(RuntimeException.class).when(genreRepository).findAll();

        var genresDto = genreService.findAll();
        assertThat(genresDto).hasSize(1)
                .element(0)
                .matches(dto -> -1L == dto.getId())
                .matches(dto -> "Unknown genre".equals(dto.getName()));
    }
}
