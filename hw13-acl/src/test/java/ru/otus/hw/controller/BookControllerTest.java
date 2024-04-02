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
import org.springframework.validation.BindingResult;
import ru.otus.hw.config.SecurityProperties;
import ru.otus.hw.convertors.BookMapper;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.BookServiceImpl;
import ru.otus.hw.services.GenreService;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
@Import({SecurityConfiguration.class, SecurityProperties.class})
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @SpyBean(classes = BookServiceImpl.class)
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private BookMapper bookMapper;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private PersistentTokenRepository persistentTokenRepository;

    @WithMockUser
    @Test
    void shouldReturnCorrectBookList() throws Exception {
        BookDto book = new BookDto(1, "Title_of_book_1",
                new AuthorDto(0, "Author"),
                new GenreDto(0, "Genre"));
        given(bookService.findAll()).willReturn(Collections.singletonList(book));

        mvc.perform(get("/"))
                .andExpect(view().name("book/list"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", Matchers.hasSize(1)))
                .andExpect(content().string(containsString("Title_of_book_1")));
    }

    @Test
    void shouldRedirectFromMainToLoginPageIfNotAuth() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @WithMockUser
    @Test
    void shouldOpenBookForEditById() throws Exception {
        BookDto book = new BookDto(111, "Title_of_book_111",
                new AuthorDto(7, "Author"),
                new GenreDto(9, "Genre"));
        willReturn(book).given(bookService).findById(111L);
        given(authorService.findAll()).willReturn(List.of(
                new AuthorDto(0, "0"),
                new AuthorDto(1, "1")));
        given(genreService.findAll()).willReturn(List.of(new GenreDto(0, "0")));

        mvc.perform(get("/book/111"))
                .andExpect(view().name("book/form_edit_book"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", Matchers.is(new BookUpdateDto(book.getId(), book.getTitle(), 7L, 9L))))
                .andExpect(model().attribute("authors", Matchers.hasSize(2)))
                .andExpect(model().attribute("genres", Matchers.hasSize(1)))
                .andExpect(content().string(containsString("Редактирование книги")))
                .andExpect(content().string(containsString("Title_of_book_111")));
    }

    @Test
    void shouldRedirectFromBookToLoginPageIfNotAuth() throws Exception {
        mvc.perform(get("/book/111"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @WithMockUser
    @Test
    void should_response_404_when_book_not_exists() throws Exception {
        doThrow(NotFoundException.class).when(bookService).findById(12345);
        mvc.perform(get("/book/12345"))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(authorities = "BOOK_DELETE")
    @Test
    void shouldDeleteBook() throws Exception {
        mvc.perform(delete("/book/delete/777"));
        verify(bookService, only()).deleteById(777);
    }

    @WithMockUser
    @Test
    void denyDeleteBookWhenAuthorityAbsent() throws Exception {
        mvc.perform(delete("/book/delete/777"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldRedirectFromDeleteToLoginPageIfNotAuth() throws Exception {
        mvc.perform(delete("/book/delete/777"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
        verify(bookService, never()).deleteById(777);
    }

    @WithMockUser(authorities = {"ROLE_USER", "BOOK_MODIFY"})
    @Test
    void shouldUpdateBook() throws Exception {
        mvc.perform(put("/book/1")
                .param("id", "1")
                .param("title", "test_updated_book")
                .param("authorId", "2")
                .param("genreId", "3"));
        verify(bookService, only()).update(new BookUpdateDto(1L, "test_updated_book", 2L, 3L));
    }

    @WithMockUser
    @Test
    void denyUpdateBookWhenAuthorityEmpty() throws Exception {
        mvc.perform(put("/book/1")
                        .param("id", "1")
                        .param("title", "test_updated_book")
                        .param("authorId", "2")
                        .param("genreId", "3"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldRedirectFromUpdateToLoginPageIfNotAuth() throws Exception {
        mvc.perform(put("/book/1")
                .param("id", "1")
                .param("title", "test_updated_book")
                .param("authorId", "2")
                .param("genreId", "3"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
        verify(bookService, never()).update(any());
    }

    @WithMockUser
    @Test
    void should_not_update_book_with_blank_title() throws Exception {
        mvc.perform(put("/book/17")
                .param("id", "17")
                .param("title", "  ")
                .param("authorId", "2")
                .param("genreId", "3"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/book/17"))
                .andExpect(flash().attributeExists("book", BindingResult.MODEL_KEY_PREFIX + "book"));
    }

    @WithMockUser
    @Test
    void should_not_update_book_with_one_char_title() throws Exception {
        mvc.perform(put("/book/1")
                .param("id", "1")
                .param("title", "F")
                .param("authorId", "2")
                .param("genreId", "3"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/book/1"))
                .andExpect(flash().attributeExists("book", BindingResult.MODEL_KEY_PREFIX + "book"));
    }

    @WithMockUser
    @Test
    void should_not_update_book_when_author_empty() throws Exception {
        mvc.perform(put("/book/1")
                .param("id", "1")
                .param("title", "Book without author")
                .param("genreId", "23"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/book/1"))
                .andExpect(flash().attributeExists("book", BindingResult.MODEL_KEY_PREFIX + "book"));
    }

    @WithMockUser
    @Test
    void should_not_update_book_when_genre_empty() throws Exception {
        mvc.perform(put("/book/1")
                .param("id", "1")
                .param("title", "Book without genre")
                .param("authorId", "13"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/book/1"))
                .andExpect(flash().attributeExists("book", BindingResult.MODEL_KEY_PREFIX + "book"));
    }

    @WithMockUser(authorities = {"ROLE_USER", "BOOK_MODIFY"})
    @Test
    void shouldCreateBook() throws Exception {
        mvc.perform(post("/book")
                .param("title", "test_new_create_book")
                .param("authorId", "33")
                .param("genreId", "22"));
        verify(bookService, only()).create(new BookCreateDto("test_new_create_book", 33L, 22L));
    }

    @WithMockUser
    @Test
    void denyCreateBookWhenAuthorityAbsent() throws Exception {
        mvc.perform(post("/book")
                        .param("title", "test_new_create_book")
                        .param("authorId", "33")
                        .param("genreId", "22"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldRedirectFromCreateToLoginPageIfNotAuth() throws Exception {
        mvc.perform(post("/book")
                .param("title", "test_new_create_book")
                .param("authorId", "33")
                .param("genreId", "22"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
        verify(bookService, never()).create(any());
    }

    @WithMockUser
    @Test
    void should_not_create_book_with_blank_title() throws Exception {
        mvc.perform(post("/book")
                .param("title", " ")
                .param("authorId", "33")
                .param("genreId", "22"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/book"))
                .andExpect(flash().attributeExists("book", BindingResult.MODEL_KEY_PREFIX + "book"));
    }

    @WithMockUser
    @Test
    void should_not_create_book_with_one_char_title() throws Exception {
        mvc.perform(post("/book")
                .param("title", "r")
                .param("authorId", "33")
                .param("genreId", "22"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/book"))
                .andExpect(flash().attributeExists("book", BindingResult.MODEL_KEY_PREFIX + "book"));
    }

    @WithMockUser
    @Test
    void should_not_create_book_when_empty_author() throws Exception {
        mvc.perform(post("/book")
                .param("title", "Test book without author")
                .param("genreId", "22"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/book"))
                .andExpect(flash().attributeExists("book", BindingResult.MODEL_KEY_PREFIX + "book"));
    }

    @WithMockUser
    @Test
    void should_not_create_book_when_empty_genre() throws Exception {
        mvc.perform(post("/book")
                .param("title", "Test book without genre")
                .param("authorId", "333"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/book"))
                .andExpect(flash().attributeExists("book", BindingResult.MODEL_KEY_PREFIX + "book"));
    }

    @WithMockUser
    @Test
    void shouldCreateNewBook() throws Exception {
        given(authorService.findAll()).willReturn(List.of(
                new AuthorDto(0, "0"),
                new AuthorDto(1, "1")));
        given(genreService.findAll()).willReturn(List.of(new GenreDto(0, "0")));

        mvc.perform(get("/book"))
                .andExpect(view().name("book/form_new_book"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("authors", Matchers.hasSize(2)))
                .andExpect(model().attribute("genres", Matchers.hasSize(1)))
                .andExpect(content().string(containsString("Добавление новой книги")))
                .andExpect(content().string(not(containsString("Редактирование книги"))));
    }
}
