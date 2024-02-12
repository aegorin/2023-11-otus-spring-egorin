package ru.otus.hw.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                new AuthorDto(0, "author"),
                new GenreDto(0, "genre"));
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
                new AuthorDto(111, "author"),
                new GenreDto(111, "genre"));
        given(bookService.findById(111L)).willReturn(book);
        given(authorService.findAll()).willReturn(List.of(
                new AuthorDto(0, "0"),
                new AuthorDto(1, "1")));
        given(genreService.findAll()).willReturn(List.of(new GenreDto(0, "0")));

        mvc.perform(get("/book/111"))
                .andExpect(view().name("book/form"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", Matchers.is(book)))
                .andExpect(model().attribute("authors", Matchers.hasSize(2)))
                .andExpect(model().attribute("genres", Matchers.hasSize(1)))
                .andExpect(content().string(containsString("Редактирование книги")))
                .andExpect(content().string(containsString("Title_of_book_111")))
                .andExpect(content().string(not(containsString("form_create_new_book"))));
    }

    @Test
    void shouldDeleteBook() throws Exception {
        mvc.perform(get("/book/777/delete"));
        verify(bookService, only()).deleteById(777);
    }

    @Test
    void shouldUpdateBook() throws Exception {
        mvc.perform(post("/book")
                .param("id", "1")
                .param("title", "test_updated_book")
                .param("author.id", "2")
                .param("genre.id", "3"));
        verify(bookService, only()).update(1, "test_updated_book", 2, 3);
    }

    @Test
    void shouldCreateBook() throws Exception {
        mvc.perform(post("/book")
                .param("form_create_new_book", "true")
                .param("title", "test_new_create_book")
                .param("author.id", "33")
                .param("genre.id", "22"));
        verify(bookService, only()).create("test_new_create_book", 33, 22);
    }

    @Test
    void shouldCreateNewBook() throws Exception {
        given(authorService.findAll()).willReturn(List.of(
                new AuthorDto(0, "0"),
                new AuthorDto(1, "1")));
        given(genreService.findAll()).willReturn(List.of(new GenreDto(0, "0")));

        mvc.perform(get("/book"))
                .andExpect(view().name("book/form"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("authors", Matchers.hasSize(2)))
                .andExpect(model().attribute("genres", Matchers.hasSize(1)))
                .andExpect(content().string(containsString("Добавление новой книги")))
                .andExpect(content().string(not(containsString("Редактирование книги"))))
                .andExpect(content().string(containsString("form_create_new_book")));
    }
}
