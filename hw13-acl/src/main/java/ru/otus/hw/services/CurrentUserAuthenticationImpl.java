package ru.otus.hw.services;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.otus.hw.security.CurrentUserDetails;

import java.util.Optional;

@Service
public class CurrentUserAuthenticationImpl implements CurrentUserAuthentication {

    @Override
    public Optional<CurrentUserDetails> getCurrentUserDetails() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(a -> (CurrentUserDetails) a.getPrincipal());
    }
}
