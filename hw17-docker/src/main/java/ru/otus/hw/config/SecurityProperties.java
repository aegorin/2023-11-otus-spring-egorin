package ru.otus.hw.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public record SecurityProperties(
        @Value("${book-application.security.validTokenRememberMe:1209600}") int validTokenRememberMe,
        @Value("${book-application.security.persistentTokenKey}") String persistentTokenKey,
        @Value("${book-application.security.nameCookieRememberMe:remember-me}") String rememberMeCookie) {}
