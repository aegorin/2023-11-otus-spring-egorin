package ru.otus.hw.services;

import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentUpdateDto;

import java.util.List;

public interface CommentService {
    List<CommentUpdateDto> findByBookId(long bookId);

    void updateComment(CommentUpdateDto commentUpdateDto);

    CommentUpdateDto addComment(CommentCreateDto commentCreateDto);

    void deleteCommentById(long commentId);
}
