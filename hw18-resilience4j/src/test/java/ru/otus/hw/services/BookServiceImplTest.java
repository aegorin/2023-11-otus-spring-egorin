package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @Test
    void findBookByIdCorrect() {
        var book = new Book(10L, "Book10", new Author(1L, "Author"), new Genre(3L, "Genre"));
        when(bookRepository.findById(10L)).thenReturn(Optional.of(book));

        assertThat(bookService.findById(10L))
                .matches(dto -> book.getId() == dto.getId())
                .matches(dto -> book.getTitle().equals(dto.getTitle()));
    }

    @Test
    void findAllCorrect() {
        var book = new Book(10L, "Book10", new Author(1L, "Author"), new Genre(3L, "Genre"));
        when(bookRepository.findAll()).thenReturn(List.of(book));

        assertThat(bookService.findAll())
                .hasSize(1)
                .element(0)
                .matches(dto -> book.getId() == dto.getId())
                .matches(dto -> book.getTitle().equals(dto.getTitle()));
    }

    @Test
    void findAllFallback() {
        doThrow(RuntimeException.class).when(bookRepository).findAll();

        assertThat(bookService.findAll())
                .hasSize(1)
                .element(0)
                .matches(dto -> -1L == dto.getId())
                .matches(dto -> "Unknow book".equals(dto.getTitle()));
    }
}
