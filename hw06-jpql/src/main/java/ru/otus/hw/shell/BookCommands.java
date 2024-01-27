package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.annotation.Command;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.services.BookService;

import java.util.stream.Collectors;

@Command(group = "Application commands")
@RequiredArgsConstructor
public class BookCommands {
    private final BookService bookService;

    private final BookConverter bookConverter;

    @Command(command = "ab", description = "Show all books")
    public String showAllBooks() {
        return bookService.findAll().stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @Command(command = "bbid", description = "Find book by id")
    public String findBookById(long id) {
        return bookService.findById(id)
                .map(bookConverter::bookToString)
                .orElse("Book with id %d not found".formatted(id));
    }

    @Command(command = "bins", description = "Insert book")
    public String insertBook(String title, long authorId, long genreId) {
        var savedBook = bookService.insert(title, authorId, genreId);
        return bookConverter.bookToString(savedBook);
    }

    @Command(command = "bupd", description = "Update book")
    public String updateBook(long id, String title, long authorId, long genreId) {
        var savedBook = bookService.update(id, title, authorId, genreId);
        return bookConverter.bookToString(savedBook);
    }

    @Command(command = "bdel", description = "Delete book by id")
    public void deleteBook(long id) {
        bookService.deleteById(id);
    }
}
