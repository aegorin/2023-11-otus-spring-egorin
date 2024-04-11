package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.controller.NotFoundException;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentUpdateDto;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CommentUpdateDto> findByBookId(long bookId) {
        return commentRepository.findByBookId(bookId).stream()
                .map(c -> new CommentUpdateDto(c.getId(), c.getText()))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentUpdateDto updateComment(CommentUpdateDto commentUpdateDto) {
        long commentId = commentUpdateDto.getId();
        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id %d not found".formatted(commentId)));
        comment.setText(comment.getText());
        comment = commentRepository.save(comment);
        return new CommentUpdateDto(comment.getId(), comment.getText());
    }

    @Transactional
    @Override
    public CommentUpdateDto addComment(CommentCreateDto commentCreateDto) {
        long bookId = commentCreateDto.getBookId();
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(bookId)));
        Comment comment = new Comment(book, commentCreateDto.getText());
        comment = commentRepository.save(comment);
        return new CommentUpdateDto(comment.getId(), comment.getText());
    }

    @Transactional
    @Override
    public void deleteCommentById(long commentId) {
        commentRepository.deleteById(commentId);
    }
}
