package ru.otus.hw.services;

import ru.otus.hw.security.CurrentUserDetails;

import java.util.Optional;

public interface CurrentUserAuthentication {

    Optional<CurrentUserDetails> getCurrentUserDetails();
}
