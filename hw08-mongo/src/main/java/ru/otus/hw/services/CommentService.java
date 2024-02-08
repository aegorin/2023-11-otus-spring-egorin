package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> findByBookId(String bookId);

    CommentDto updateComment(String commentId, String commentText);

    CommentDto addComment(String bookId, String commentText);

    void deleteCommentById(String commentId);
}
