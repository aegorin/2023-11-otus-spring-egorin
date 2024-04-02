package ru.otus.hw.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import java.util.Date;

@SpringBootTest
class PersistentTokenRepositoryJdbcTest {

    @Autowired
    private PersistentTokenRepositoryJdbc jdbcPersistentTokenRepository;

    @Autowired
    private JdbcOperations jdbcOperations;

    private final PersistentRememberMeToken token = new PersistentRememberMeToken("user1", "seria1", "val", new Date());

    @AfterEach
    void clear_tokens_table() {
        jdbcOperations.update("delete from user_tokens");
    }

    @Test
    void should_save_new_token() {
        var persistentToken = jdbcPersistentTokenRepository.getTokenForSeries(token.getSeries());
        Assertions.assertNull(persistentToken);

        jdbcPersistentTokenRepository.createNewToken(token);
        persistentToken = jdbcPersistentTokenRepository.getTokenForSeries(token.getSeries());
        Assertions.assertNotNull(persistentToken);
        Assertions.assertEquals(token.getSeries(), persistentToken.getSeries());
    }

    @Test
    void should_update_token() {
        jdbcPersistentTokenRepository.createNewToken(token);
        jdbcPersistentTokenRepository.updateToken(token.getSeries(), "token", new Date());

        var persistentToken = jdbcPersistentTokenRepository.getTokenForSeries(token.getSeries());
        Assertions.assertNotNull(persistentToken);
        Assertions.assertEquals(token.getSeries(), persistentToken.getSeries());
        Assertions.assertEquals(token.getUsername(), persistentToken.getUsername());
        Assertions.assertNotEquals(token.getTokenValue(), persistentToken.getTokenValue());
        Assertions.assertNotEquals(token.getDate(), persistentToken.getDate());
    }

    @Test
    void should_return_token_by_username() {
        var persistentToken = jdbcPersistentTokenRepository.getTokenForSeries(token.getSeries());
        Assertions.assertNull(persistentToken);

        jdbcPersistentTokenRepository.createNewToken(token);
        persistentToken = jdbcPersistentTokenRepository.getTokenForSeries(token.getSeries());
        Assertions.assertNotNull(persistentToken);

        jdbcPersistentTokenRepository.removeUserTokens("bad_user_name");
        persistentToken = jdbcPersistentTokenRepository.getTokenForSeries(token.getSeries());
        Assertions.assertNotNull(persistentToken);

        jdbcPersistentTokenRepository.removeUserTokens(token.getUsername());
        persistentToken = jdbcPersistentTokenRepository.getTokenForSeries(token.getSeries());
        Assertions.assertNull(persistentToken);
    }
}
