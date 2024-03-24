package ru.otus.hw.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.hw.repositories.LoginUserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceJpa implements UserDetailsService {

    private final LoginUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username)
                .map(u -> User.builder()
                        .username(u.getLogin())
                        .password(u.getPassword())
                        .disabled(u.isBlocked())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User with login %s not found".formatted(username)));
    }
}
