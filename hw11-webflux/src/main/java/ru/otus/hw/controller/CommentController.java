package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentUpdateDto;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.CommentRepository;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentRepository commentRepository;

    @GetMapping("/api/v1/book/{bookId}/comment")
    public Flux<CommentUpdateDto> allCommentsForBook(@PathVariable Long bookId) {
        return commentRepository.findByBookId(bookId)
                .map(comment -> new CommentUpdateDto(comment.getId(), comment.getText()));
    }

    @PostMapping("/api/v1/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CommentUpdateDto> createNewComment(@Valid @RequestBody CommentCreateDto commentCreateDto) {
        return commentRepository.save(new Comment(commentCreateDto.getText(), commentCreateDto.getBookId()))
                .map(c -> new CommentUpdateDto(c.getId(), c.getText()));
    }

    @PutMapping("/api/v1/comment/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> updateCommentById(@Valid @RequestBody CommentUpdateDto commentUpdateDto) {
        long commentId = commentUpdateDto.getId();
        return commentRepository.findById(commentId)
                .flatMap(c -> {
                    c.setText(commentUpdateDto.getText());
                    return commentRepository.save(c);
                })
                .flatMap(comment -> Mono.empty());
    }

    @DeleteMapping("/api/v1/comment/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCommentById(@PathVariable Long commentId) {
        return commentRepository.deleteById(commentId);
    }
}
