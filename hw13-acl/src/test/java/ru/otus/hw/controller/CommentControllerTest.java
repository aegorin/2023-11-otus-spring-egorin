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
import ru.otus.hw.convertors.BookMapper;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentUpdateDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.UserRepository;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.BookServiceImpl;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.CurrentUserAuthentication;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(CommentController.class)
@Import({SecurityConfiguration.class, SecurityProperties.class})
class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @SpyBean(classes = BookServiceImpl.class)
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private BookMapper bookMapper;

    @MockBean
    private CommentService commentService;

    @MockBean
    private PersistentTokenRepository persistentTokenRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CurrentUserAuthentication currentUserAuthentication;

    @WithMockUser
    @Test
    void shouldReturnAllCommentsForBook() throws Exception {
        BookDto book = new BookDto(1, "Title_of_book_1", new AuthorDto(), new GenreDto());
        willReturn(book).given(bookService).findById(1);
        given(commentService.findByBookId(1)).willReturn(List.of(
                new CommentUpdateDto(1L, "1"),
                new CommentUpdateDto(2L, "2")));

        mvc.perform(get("/book/comment/1"))
                .andExpect(view().name("comment/list"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", Matchers.equalTo(book)))
                .andExpect(model().attribute("comments", Matchers.hasSize(2)));
    }

    @WithMockUser(authorities = "WRONG")
    @Test
    void forbiddenWhenBadAuthorities() throws Exception {
        mvc.perform(get("/book/comment/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldRedirectToLoginPageWhenNotAuth() throws Exception {
        mvc.perform(get("/book/comment/{bookId}", 1))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}
