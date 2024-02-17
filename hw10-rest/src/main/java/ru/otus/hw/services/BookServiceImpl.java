package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.controller.NotFoundException;
import ru.otus.hw.convertors.BookMapper;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Override
    @Transactional(readOnly = true)
    public BookDto findById(long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new NotFoundException("bookId", "Book with id %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public BookUpdateDto create(BookCreateDto bookCreateDto) {
        var book = bookMapper.toModel(bookCreateDto);
        book = bookRepository.save(book);
        return bookMapper.toUpdateDto(book);
    }

    @Override
    @Transactional
    public void update(BookUpdateDto bookUpdateDto) {
        if (!bookRepository.existsById(bookUpdateDto.getId())) {
            throw new NotFoundException("bookId", "Book with id %d not found".formatted(bookUpdateDto.getId()));
        }
        var book = bookMapper.toModel(bookUpdateDto);
        bookRepository.save(book);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }
}
