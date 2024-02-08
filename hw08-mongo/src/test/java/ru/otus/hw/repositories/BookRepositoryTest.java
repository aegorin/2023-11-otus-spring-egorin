package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

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
        var actualBook = bookRepository.findById("1");
        var expectedBook = mongoTemplate.findById("1", Book.class);
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

        assertThat(actualBooks)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(expectedBooks);
    }
    
    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var expectedBook = new Book(null, "BookTitle_10500", dbAuthors.get(0), dbGenres.get(0));
        expectedBook = bookRepository.save(expectedBook);

        var actualBook = mongoTemplate.findById(expectedBook.getId(), Book.class);
        assertThat(expectedBook)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(actualBook);
    }
    
    @DisplayName("должен сохранять изменённую книгу")
    @Test
    void shouldSaveUpdatedBook() {
        final var expectedBook = new Book("1", "BookTitle_10500", dbAuthors.get(2), dbGenres.get(2));
        var actualBook = mongoTemplate.findById(expectedBook.getId(), Book.class);

        assertThat(actualBook)
                .isNotNull()
                .isNotEqualTo(expectedBook);

        bookRepository.save(expectedBook);
        actualBook = mongoTemplate.findById(expectedBook.getId(), Book.class);

        assertThat(actualBook)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteBook() {
        var firstBook = mongoTemplate.findById("1", Book.class);
        assertThat(firstBook).isNotNull();

        bookRepository.deleteById(firstBook.getId());

        assertThat(mongoTemplate.findById("1", Book.class)).isNull();
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(Integer.toString(id), "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Genre(Integer.toString(id), "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(Integer.toString(id), "Book_" + id, dbAuthors.get(id - 1), dbGenres.get(id - 1)))
                .toList();
    }
}
