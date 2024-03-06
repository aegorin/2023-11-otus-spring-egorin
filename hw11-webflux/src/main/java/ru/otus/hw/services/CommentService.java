package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentUpdateDto;

public interface CommentService {

    Flux<CommentUpdateDto> findByBookId(Long bookId);

    Mono<Void> updateComment(CommentUpdateDto commentUpdateDto);

    Mono<CommentUpdateDto> addComment(CommentCreateDto commentCreateDto);

    Mono<Void> deleteCommentById(Long commentId);
}
