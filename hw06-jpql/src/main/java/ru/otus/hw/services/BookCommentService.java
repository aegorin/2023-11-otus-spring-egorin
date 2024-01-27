package ru.otus.hw.services;

import ru.otus.hw.dto.BookCommentDto;

import java.util.List;

public interface BookCommentService {
    List<BookCommentDto> findByBookId(long bookId);

    BookCommentDto updateBookComment(long commentId, String commentText);

    BookCommentDto addBookComment(long bookId, String commentText);

    void deleteCommentById(long commentId);
}
