package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.convertors.BookMapper;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.repositories.BookRepository;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Override
    @Transactional(readOnly = true)
    public Mono<BookUpdateDto> findById(Long bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toUpdateDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<BookDto> findAllBooksDto() {
        return bookRepository.findAllBooksDto()
                .map(bookMapper::toBookDto);
    }

    @Override
    @Transactional
    public Mono<BookUpdateDto> create(BookCreateDto bookCreateDto) {
        return Mono.just(bookMapper.toModel(bookCreateDto))
                .flatMap(bookRepository::save)
                .map(bookMapper::toUpdateDto);
    }

    @Override
    @Transactional
    public Mono<Void> update(BookUpdateDto bookUpdateDto) {
        return Mono.just(bookMapper.toModel(bookUpdateDto))
                .flatMap(bookRepository::save)
                .flatMap(b -> Mono.empty());
    }

    @Override
    @Transactional
    public Mono<Void> deleteById(Long bookId) {
        return bookRepository.deleteById(bookId);
    }
}
