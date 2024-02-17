package ru.otus.hw.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

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
    void shouldEditBookById() throws Exception {
        BookDto book = new BookDto(111, "Title_of_book_111",
                new AuthorDto(7, "Author"),
                new GenreDto(9, "Genre"));
        given(bookService.findById(111L)).willReturn(book);
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
    void should_response_404_when_book_not_exists() throws Exception {
        doThrow(NotFoundException.class).when(bookService).findById(12345);
        mvc.perform(get("/book/12345"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteBook() throws Exception {
        mvc.perform(delete("/book/delete/777"));
        verify(bookService, only()).deleteById(777);
    }

    @Test
    void shouldUpdateBook() throws Exception {
        mvc.perform(put("/book")
                .param("id", "1")
                .param("title", "test_updated_book")
                .param("authorId", "2")
                .param("genreId", "3"));
        verify(bookService, only()).update(new BookUpdateDto(1L, "test_updated_book", 2L, 3L));
    }

    @Test
    void should_not_update_book_with_blank_title() throws Exception {
        mvc.perform(put("/book")
                .param("id", "1")
                .param("title", "  ")
                .param("authorId", "2")
                .param("genreId", "3"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_update_book_with_one_char_title() throws Exception {
        mvc.perform(put("/book")
                .param("id", "1")
                .param("title", "F")
                .param("authorId", "2")
                .param("genreId", "3"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_update_book_when_author_empty() throws Exception {
        mvc.perform(put("/book")
                .param("id", "1")
                .param("title", "Book without author")
                .param("genreId", "23"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_update_book_when_genre_empty() throws Exception {
        mvc.perform(put("/book")
                .param("id", "1")
                .param("title", "Book without genre")
                .param("authorId", "13"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateBook() throws Exception {
        mvc.perform(post("/book")
                .param("title", "test_new_create_book")
                .param("authorId", "33")
                .param("genreId", "22"));
        verify(bookService, only()).create(new BookCreateDto("test_new_create_book", 33L, 22L));
    }

    @Test
    void should_not_create_book_with_blank_title() throws Exception {
        mvc.perform(post("/book")
                .param("title", " ")
                .param("authorId", "33")
                .param("genreId", "22"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_create_book_with_one_char_title() throws Exception {
        mvc.perform(post("/book")
                .param("title", "r")
                .param("authorId", "33")
                .param("genreId", "22"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_create_book_when_empty_author() throws Exception {
        mvc.perform(post("/book")
                .param("title", "Test book without author")
                .param("genreId", "22"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_create_book_when_empty_genre() throws Exception {
        mvc.perform(post("/book")
                .param("title", "Test book without genre")
                .param("authorId", "333"))
                .andExpect(status().isBadRequest());
    }

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
