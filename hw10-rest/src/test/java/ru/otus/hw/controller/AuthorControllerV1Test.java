package ru.otus.hw.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
class AuthorControllerV1Test {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService authorService;

    @Test
    void shouldReturnCorrectGenresList() throws Exception {
        List<AuthorDto> authors = Arrays.asList(
                new AuthorDto(111, "Author Test 1"),
                new AuthorDto(222, "Author Test 2"),
                new AuthorDto(333, "Author Test 3"));
        given(authorService.findAll()).willReturn(authors);

        mvc.perform(get("/api/v1/author"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[1].id", Matchers.is(222)))
                .andExpect(jsonPath("$[1].fullName", Matchers.is("Author Test 2")));
    }
}
