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
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.AuthorServiceImpl;

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

@WebMvcTest(AuthorController.class)
@Import({SecurityConfiguration.class, SecurityProperties.class})
class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @SpyBean(value = AuthorServiceImpl.class)
    private AuthorService authorService;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private PersistentTokenRepository persistentTokenRepository;

    @WithMockUser
    @Test
    void shouldReturnCorrectAuthorsList() throws Exception {
        List<AuthorDto> authors = Arrays.asList(
                new AuthorDto(1, "Author Test 1"),
                new AuthorDto(2, "Author Test 2"),
                new AuthorDto(3, "Author Test 3"));
        given(authorService.findAll()).willReturn(authors);

        mvc.perform(get("/authors"))
                .andExpect(view().name("author/list"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("authors", Matchers.hasSize(3)))
                .andExpect(content().string(containsString("Author Test 1")))
                .andExpect(content().string(containsString("Author Test 2")))
                .andExpect(content().string(containsString("Author Test 3")));
    }

    @WithMockUser(roles = "WRONG")
    @Test
    void forbiddenWhenNotUserAuthorsList() throws Exception {
        mvc.perform(get("/authors"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldRedirectToLoginPageWhenNotAuth() throws Exception {
        mvc.perform(get("/authors"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}
