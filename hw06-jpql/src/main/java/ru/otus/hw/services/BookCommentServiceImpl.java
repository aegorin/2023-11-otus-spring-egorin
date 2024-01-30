package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookCommentConverter;
import ru.otus.hw.dto.BookCommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.repositories.BookCommentRepository;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookCommentServiceImpl implements BookCommentService {

    private final BookRepository bookRepository;

    private final BookCommentRepository bookCommentRepository;

    private final BookCommentConverter bookCommentConverter;

    @Transactional(readOnly = true)
    @Override
    public List<BookCommentDto> findByBookId(long bookId) {
        return bookCommentRepository.findByBookId(bookId).stream()
                .map(bookCommentConverter::from)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public BookCommentDto updateBookComment(long commentId, String commentText) {
        var comment = bookCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(commentId)));
        comment.setComment(commentText);
        comment = bookCommentRepository.save(comment);
        return bookCommentConverter.from(comment);
    }

    @Transactional
    @Override
    public BookCommentDto addBookComment(long bookId, String commentText) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        BookComment bookComment = new BookComment(book);
        bookComment.setComment(commentText);
        bookComment = bookCommentRepository.save(bookComment);
        return bookCommentConverter.from(bookComment);
    }

    @Transactional
    @Override
    public void deleteCommentById(long commentId) {
        bookCommentRepository.deleteById(commentId);
    }
}
