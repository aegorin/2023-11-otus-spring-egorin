package ru.otus.hw.services;

import ru.otus.hw.dto.CommentUpdateDto;

import java.util.List;

public interface CommentService {
    List<CommentUpdateDto> findByBookId(long bookId);

    CommentUpdateDto updateComment(long commentId, String commentText);

    CommentUpdateDto addComment(long bookId, String commentText);

    void deleteCommentById(long commentId);
}
