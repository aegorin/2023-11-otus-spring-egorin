package ru.otus.hw.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.otus.hw.models.User;
import ru.otus.hw.repositories.UserRepository;

import java.util.Optional;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Import(UserDetailsServiceJpa.class)
class UserDetailsServiceJpaTest {

    @Autowired
    private UserDetailsService userDetailsService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void should_throw_exception_when_user_absent() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("none"));
    }

    @Test
    void should_load_user_by_login() {
        var username = "user";
        var user = new User();
        user.setLogin(username);
        user.setPassword("pwd");

        Mockito.when(userRepository.findByLogin(username)).thenReturn(Optional.of(user));

        var details = userDetailsService.loadUserByUsername(username);
        Assertions.assertNotNull(details);
        Assertions.assertEquals("user", details.getUsername());
        Mockito.verify(userRepository, Mockito.only()).findByLogin(username);
    }
}