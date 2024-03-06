package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentUpdateDto;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.CommentRepository;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public Flux<CommentUpdateDto> findByBookId(Long bookId) {
        return commentRepository.findByBookId(bookId)
                .map(comment -> new CommentUpdateDto(comment.getId(), comment.getText()));
    }

    @Override
    @Transactional
    public Mono<Void> updateComment(CommentUpdateDto commentUpdateDto) {
        long commentId = commentUpdateDto.getId();
        return commentRepository.findById(commentId)
                .flatMap(c -> {
                    c.setText(commentUpdateDto.getText());
                    return commentRepository.save(c);
                })
                .flatMap(comment -> Mono.empty());
    }

    @Override
    @Transactional
    public Mono<CommentUpdateDto> addComment(CommentCreateDto commentCreateDto) {
        return commentRepository.save(new Comment(commentCreateDto.getText(), commentCreateDto.getBookId()))
                .map(c -> new CommentUpdateDto(c.getId(), c.getText()));
    }

    @Override
    @Transactional
    public Mono<Void> deleteCommentById(Long commentId) {
        return commentRepository.deleteById(commentId);
    }
}
