package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
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

    private final CommentConverter commentConverter;

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto>findByBookId(String bookId) {
        return commentRepository.findByBookId(bookId).stream()
                .map(commentConverter::from)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentDto updateComment(String commentId, String commentText) {
        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %s not found".formatted(commentId)));
        comment.setComment(commentText);
        comment = commentRepository.save(comment);
        return commentConverter.from(comment);
    }

    @Transactional
    @Override
    public CommentDto addComment(String bookId, String commentText) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        Comment comment = new Comment(book);
        comment.setComment(commentText);
        comment = commentRepository.save(comment);
        return commentConverter.from(comment);
    }

    @Transactional
    @Override
    public void deleteCommentById(String commentId) {
        commentRepository.deleteById(commentId);
    }
}
