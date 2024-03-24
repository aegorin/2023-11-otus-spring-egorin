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
import ru.otus.hw.models.LoginUser;
import ru.otus.hw.repositories.LoginUserRepository;

import java.util.Optional;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Import(UserDetailsServiceJpa.class)
class UserDetailsServiceJpaTest {

    @Autowired
    private UserDetailsService userDetailsService;

    @MockBean
    private LoginUserRepository userRepository;

    @Test
    void should_throw_exception_when_user_absent() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("none"));
    }

    @Test
    void should_load_user_by_login() {
        var username = "user";
        var resultLoginUser = new LoginUser();
        resultLoginUser.setLogin(username);
        resultLoginUser.setPassword("pwd");

        Mockito.when(userRepository.findByLogin(username)).thenReturn(Optional.of(resultLoginUser));

        var details = userDetailsService.loadUserByUsername(username);
        Assertions.assertNotNull(details);
        Assertions.assertEquals("user", details.getUsername());
        Mockito.verify(userRepository, Mockito.only()).findByLogin(username);
    }
}