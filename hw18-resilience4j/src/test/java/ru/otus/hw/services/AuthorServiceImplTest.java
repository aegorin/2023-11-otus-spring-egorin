package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthorServiceImplTest {

    @Autowired
    private AuthorService authorService;

    @MockBean
    private AuthorRepository authorRepository;

    @Test
    void shouldReturnCorrectList() {
        var author = new Author(1L, "Author1");
        when(authorRepository.findAll()).thenReturn(List.of(author));

        var authorDto = authorService.findAll();
        assertThat(authorDto).hasSize(1)
                .element(0)
                .matches(dto -> author.getId() == dto.getId() &&
                                author.getFullName().equals(dto.getFullName()));
    }

    @Test
    void shouldReturnDefaultListWhenError() {
        doThrow(RuntimeException.class).when(authorRepository).findAll();

        var authorDto = authorService.findAll();
        assertThat(authorDto).hasSize(1)
                .element(0)
                .matches(dto -> -1L == dto.getId() &&
                                "Unknown".equals(dto.getFullName()));
    }
}
