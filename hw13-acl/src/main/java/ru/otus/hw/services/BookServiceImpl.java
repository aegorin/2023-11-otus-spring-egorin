package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.controller.NotFoundException;
import ru.otus.hw.convertors.BookMapper;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.UserRepository;
import ru.otus.hw.security.CurrentUserDetails;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final UserRepository userRepository;

    private final CurrentUserAuthentication currentUserAuthentication;

    @Override
    @Transactional(readOnly = true)
    public BookDto findById(long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(id)));
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
        currentUserAuthentication.getCurrentUserDetails()
                .map(CurrentUserDetails::getUserId)
                .flatMap(userRepository::findById)
                .ifPresent(book::setOwner);
        book = bookRepository.save(book);
        return bookMapper.toUpdateDto(book);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('MANAGER') or @ownerOfBookChecker.isCurrentUserOwnerOfBook(#bookDto)")
    public BookUpdateDto update(@P("bookDto") BookUpdateDto bookUpdateDto) {
        var savedBook = bookRepository.findById(bookUpdateDto.getId())
                .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(bookUpdateDto.getId())));
        var book = bookMapper.toModel(bookUpdateDto);
        book.setOwner(savedBook.getOwner());
        book = bookRepository.save(book);
        return bookMapper.toUpdateDto(book);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('MANAGER') or @ownerOfBookChecker.isCurrentUserOwnerOfBook(#bookId)")
    public void deleteById(@P("bookId") long id) {
        bookRepository.deleteById(id);
    }
}
