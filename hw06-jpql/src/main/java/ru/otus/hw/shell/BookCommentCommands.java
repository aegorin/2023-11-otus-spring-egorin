package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.annotation.Command;
import ru.otus.hw.converters.BookCommentConverter;
import ru.otus.hw.services.BookCommentService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Command(group = "Book comment commands")
public class BookCommentCommands {

    private final BookCommentService bookCommentService;

    private final BookCommentConverter bookCommentConverter;

    @Command(command = "bcs", description = "Show comments for book")
    public String showBookComments(long bookId) {
        var comments = bookCommentService.findByBookId(bookId)
                .stream()
                .map(bookCommentConverter::bookCommentToString)
                .collect(Collectors.toList());
        if (comments.isEmpty()) {
            return "No comments available";
        }
        return String.join(System.lineSeparator(), comments);
    }

    @Command(command = "bcu", description = "Edit comment for book")
    public String updateBookComment(long commentId, String changedText) {
        var bookComment = bookCommentService.updateBookComment(commentId, changedText);
        return bookCommentConverter.bookCommentToString(bookComment);
    }

    @Command(command = "bcd", description = "Delete comment for book")
    public void deleteBookComment(long commentId) {
        bookCommentService.deleteCommentById(commentId);
    }

    @Command(command = "bcn", description = "Add new comment for book")
    public String addCommentForBook(long bookId, String commentText) {
        var bookComment = bookCommentService.addBookComment(bookId, commentText);
        return bookCommentConverter.bookCommentToString(bookComment);
    }
}
