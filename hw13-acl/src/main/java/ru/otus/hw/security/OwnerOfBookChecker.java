package ru.otus.hw.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.services.CurrentUserAuthentication;

@Component
@RequiredArgsConstructor
public class OwnerOfBookChecker {

    private final CurrentUserAuthentication currentUserAuthentication;

    private final BookRepository bookRepository;

    public boolean isCurrentUserOwnerOfBook(long bookId) {
        return isOwner(bookId);
    }

    public boolean isCurrentUserOwnerOfBook(BookUpdateDto bookUpdateDto) {
        return isOwner(bookUpdateDto.getId());
    }

    private boolean isOwner(Long bookId) {
        return currentUserAuthentication.getCurrentUserDetails()
                .map(CurrentUserDetails::getUserId)
                .map(userId -> bookRepository.existsByIdAndOwnerId(bookId, userId))
                .orElse(false);
    }
}
