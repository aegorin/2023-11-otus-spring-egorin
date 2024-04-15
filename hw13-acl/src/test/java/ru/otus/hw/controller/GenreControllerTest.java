package ru.otus.hw.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.SecurityProperties;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.GenreService;
import ru.otus.hw.services.GenreServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(GenreController.class)
@Import({SecurityConfiguration.class, SecurityProperties.class})
class GenreControllerTest {

    @Autowired
    private MockMvc mvc;

    @SpyBean(classes = GenreServiceImpl.class)
    private GenreService genreService;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private PersistentTokenRepository persistentTokenRepository;

    @WithMockUser
    @Test
    void shouldReturnCorrectGenresList() throws Exception {
        List<GenreDto> genres = Arrays.asList(
                new GenreDto(1, "genre1"),
                new GenreDto(2, "genre2"));
        given(genreService.findAll()).willReturn(genres);

        mvc.perform(get("/genres"))
                .andExpect(view().name("genre/list"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("genres", Matchers.hasSize(2)))
                .andExpect(content().string(containsString("genre1")))
                .andExpect(content().string(containsString("genre2")));
    }

    @WithMockUser(roles = "WRONG")
    @Test
    void forbiddenWhenNotUser() throws Exception {
        mvc.perform(get("/genres"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldRedirectToLoginPageWhenNotAuth() throws Exception {
        mvc.perform(get("/genres"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}
