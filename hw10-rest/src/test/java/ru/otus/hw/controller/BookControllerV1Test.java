package ru.otus.hw.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.BookService;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerV1Test {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @Test
    void shouldReturnAllBooks() throws Exception {
        BookDto book = new BookDto(1, "Title_of_book_1",
                new AuthorDto(0, "Author"),
                new GenreDto(0, "Genre"));
        given(bookService.findAll()).willReturn(Collections.singletonList(book));

        mvc.perform(get("/api/v1/book"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].title", Matchers.is(book.getTitle())));
    }

    @Test
    void should_return_correct_book_by_id() throws Exception {
        int bookId = 43211;
        var bookDto = new BookDto(bookId, "Title_of_book_1",
                new AuthorDto(0, "Author"),
                new GenreDto(0, "Genre"));
        given(bookService.findById(bookId)).willReturn(bookDto);
        mvc.perform(get("/api/v1/book/" + bookId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(bookId)))
                .andExpect(jsonPath("$.title", is(bookDto.getTitle())))
                .andExpect(jsonPath("$.genreId", is(0)))
                .andExpect(jsonPath("$.authorId", is(0)));
    }

    @Test
    void should_response_404_when_book_not_exists() throws Exception {
        doThrow(NotFoundException.class).when(bookService).findById(12345);
        mvc.perform(get("/api/v1/book/12345"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors", not(Matchers.empty())));
    }

    @Test
    void shouldDeleteBook() throws Exception {
        mvc.perform(delete("/api/v1/book/777"))
                .andExpect(status().isNoContent());
        verify(bookService, only()).deleteById(777);
    }

    @Test
    void shouldCreateBook() throws Exception {
        var bookCreateDto = new BookCreateDto("test_new_create_book", 33L, 22L);
        var bookUpdateDto = new BookUpdateDto(11L, bookCreateDto.getTitle(),
                bookCreateDto.getAuthorId(), bookCreateDto.getGenreId());
        given(bookService.create(bookCreateDto)).willReturn(bookUpdateDto);

        mvc.perform(post("/api/v1/book")
                        .content(toJsonString(bookCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(11))
                .andExpect(jsonPath("$.title").value("test_new_create_book"));
        verify(bookService, only()).create(bookCreateDto);
    }

    @Test
    void should_not_create_book_with_blank_title() throws Exception {
        var bookCreateDto = new BookCreateDto(" ", 33L, 22L);
        mvc.perform(post("/api/v1/book")
                        .content(toJsonString(bookCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors", not(Matchers.empty())))
                .andExpect(jsonPath("$.errors[0].field").value("title"))
                .andExpect(jsonPath("$.errors[0].message").isNotEmpty());
    }

    @Test
    void should_not_create_book_with_one_char_title() throws Exception {
        var bookCreateDto = new BookCreateDto("r", 33L, 22L);
        mvc.perform(post("/api/v1/book")
                        .content(toJsonString(bookCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors", not(Matchers.empty())))
                .andExpect(jsonPath("$.errors[0].field").value("title"))
                .andExpect(jsonPath("$.errors[0].message").isNotEmpty());
    }

    @Test
    void should_not_create_book_when_empty_author() throws Exception {
        var bookCreateDto = new BookCreateDto("Test book without author", null, 22L);
        mvc.perform(post("/api/v1/book")
                        .content(toJsonString(bookCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors", not(Matchers.empty())))
                .andExpect(jsonPath("$.errors[0].field").value("authorId"))
                .andExpect(jsonPath("$.errors[0].message").isNotEmpty());
    }

    @Test
    void should_not_create_book_when_empty_genre() throws Exception {
        var bookCreateDto = new BookCreateDto("Test book without genre", 333L, null);
        mvc.perform(post("/api/v1/book")
                        .content(toJsonString(bookCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors", not(Matchers.empty())))
                .andExpect(jsonPath("$.errors[0].field").value("genreId"))
                .andExpect(jsonPath("$.errors[0].message").isNotEmpty());
    }

    @Test
    void shouldUpdateBook() throws Exception {
        var bookUpdateDto = new BookUpdateDto(1L, "test_updated_book", 2L, 3L);
        mvc.perform(put("/api/v1/book/1")
                        .content(toJsonString(bookUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
        verify(bookService, only()).update(bookUpdateDto);
    }

    @Test
    void should_not_update_book_with_blank_title() throws Exception {
        var bookUpdateDto = new BookUpdateDto(1L, "  ", 2L, 3L);

        mvc.perform(put("/api/v1/book/1")
                        .content(toJsonString(bookUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors", not(Matchers.empty())))
                .andExpect(jsonPath("$.errors[0].field").value("title"))
                .andExpect(jsonPath("$.errors[0].message").isNotEmpty());

        verify(bookService, never()).update(bookUpdateDto);
    }

    @Test
    void should_not_update_book_with_one_char_title() throws Exception {
        var bookUpdateDto = new BookUpdateDto(1L, "F", 2L, 3L);

        mvc.perform(put("/api/v1/book/1")
                        .content(toJsonString(bookUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors", not(Matchers.empty())))
                .andExpect(jsonPath("$.errors[0].field").value("title"))
                .andExpect(jsonPath("$.errors[0].message").isNotEmpty());

        verify(bookService, never()).update(bookUpdateDto);
    }

    @Test
    void should_not_update_book_when_author_empty() throws Exception {
        var bookUpdateDto = new BookUpdateDto(1L, "Book without author", null, 3L);

        mvc.perform(put("/api/v1/book/1")
                        .content(toJsonString(bookUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors", not(Matchers.empty())))
                .andExpect(jsonPath("$.errors[0].field").value("authorId"))
                .andExpect(jsonPath("$.errors[0].message").isNotEmpty());

        verify(bookService, never()).update(bookUpdateDto);
    }

    @Test
    void should_not_update_book_when_genre_empty() throws Exception {
        var bookUpdateDto = new BookUpdateDto(1L, "Book without genre", 13L, null);

        mvc.perform(put("/api/v1/book/1")
                        .content(toJsonString(bookUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors", not(Matchers.empty())))
                .andExpect(jsonPath("$.errors[0].field").value("genreId"))
                .andExpect(jsonPath("$.errors[0].message").isNotEmpty());

        verify(bookService, never()).update(bookUpdateDto);
    }

    private String toJsonString(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
