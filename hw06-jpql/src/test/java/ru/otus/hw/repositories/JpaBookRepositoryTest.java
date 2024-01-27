package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DataJpaTest
@Import(JpaBookRepository.class)
class JpaBookRepositoryTest {
    @Autowired
    private JpaBookRepository bookRepository;

    @Autowired
    private TestEntityManager em;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }
    
    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        var actualBook = bookRepository.findById(1);
        var expectedBook = em.find(Book.class, 1);
        assertThat(actualBook)
                .isNotEmpty()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        List<Book> actualBooks = bookRepository.findAll();
        List<Book> expectedBooks = dbBooks;

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
    }
    
    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var expectedBook = new Book(0, "BookTitle_10500", dbAuthors.get(0), dbGenres.get(0));
        expectedBook = bookRepository.save(expectedBook);

        var actualBook = em.find(Book.class, expectedBook.getId());
        assertThat(expectedBook)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(actualBook);
    }
    
    @DisplayName("должен сохранять изменённую книгу")
    @Test
    void shouldSaveUpdatedBook() {
        final var expectedBook = new Book(1L, "BookTitle_10500", dbAuthors.get(2), dbGenres.get(2));
        var actualBook = em.find(Book.class, expectedBook.getId());
        assertThat(actualBook)
                .isNotNull()
                .isNotEqualTo(expectedBook);

        bookRepository.save(expectedBook);
        actualBook = em.find(Book.class, expectedBook.getId());

        assertThat(actualBook)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должно быть исключение если книга не обновлена в базе")
    @Test
    void exception_when_book_not_updated() {
        var notSavedBook = new Book(Integer.MIN_VALUE, "none", dbAuthors.get(1), dbGenres.get(1));
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> bookRepository.save(notSavedBook));
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        var firstBook = em.find(Book.class, 1);
        assertThat(firstBook).isNotNull();

        bookRepository.deleteById(firstBook.getId());

        assertThat(em.find(Book.class, firstBook.getId())).isNull();
    }

    @DisplayName("должно быть исключение при удалении книги по несуществующему id")
    @Test
    void shouldExceptionWhereDeleteUnpersistedBook() {
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> bookRepository.deleteById(Integer.MIN_VALUE));
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id, "Book_" + id, dbAuthors.get(id - 1), dbGenres.get(id - 1)))
                .toList();
    }
}
