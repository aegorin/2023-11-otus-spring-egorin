package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.annotation.Command;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Command(group = "Book comment commands")
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @Command(command = "bcs", description = "Show comments for book")
    public String showComments(long bookId) {
        var comments = commentService.findByBookId(bookId)
                .stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.toList());
        if (comments.isEmpty()) {
            return "No comments available";
        }
        return String.join(System.lineSeparator(), comments);
    }

    @Command(command = "bcu", description = "Edit comment for book")
    public String updateComment(long commentId, String changedText) {
        var comment = commentService.updateComment(commentId, changedText);
        return commentConverter.commentToString(comment);
    }

    @Command(command = "bcd", description = "Delete comment for book")
    public void deleteComment(long commentId) {
        commentService.deleteCommentById(commentId);
    }

    @Command(command = "bcn", description = "Add new comment for book")
    public String addCommentForBook(long bookId, String commentText) {
        var comment = commentService.addComment(bookId, commentText);
        return commentConverter.commentToString(comment);
    }
}
