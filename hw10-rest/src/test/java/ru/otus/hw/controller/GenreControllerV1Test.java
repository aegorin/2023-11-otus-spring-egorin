package ru.otus.hw.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreController.class)
class GenreControllerV1Test {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreService genreService;

    @Test
    void shouldReturnCorrectGenresList() throws Exception {
        List<GenreDto> genres = Arrays.asList(
                new GenreDto(123, "genre1"),
                new GenreDto(987, "genre2"));
        given(genreService.findAll()).willReturn(genres);

        mvc.perform(get("/api/v1/genre"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is(123)))
                .andExpect(jsonPath("$[0].name", Matchers.is("genre1")));
    }
}
