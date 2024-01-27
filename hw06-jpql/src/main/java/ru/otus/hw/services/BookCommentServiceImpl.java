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
        var optBookComment = bookCommentRepository.findById(commentId);
        if (optBookComment.isEmpty()) {
            throw new EntityNotFoundException("Comment with id %d not found".formatted(commentId));
        }
        BookComment bookComment = optBookComment.get();
        bookComment.setComment(commentText);
        bookComment = bookCommentRepository.save(bookComment);
        return bookCommentConverter.from(bookComment);
    }

    @Transactional
    @Override
    public BookCommentDto addBookComment(long bookId, String commentText) {
        var book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new EntityNotFoundException("Book with id %d not found".formatted(bookId));
        }
        BookComment bookComment = new BookComment(book.get());
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
