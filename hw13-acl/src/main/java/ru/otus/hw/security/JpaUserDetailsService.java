package ru.otus.hw.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.hw.repositories.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with login %s not found".formatted(username)));

        return User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .disabled(user.isBlocked())
                .authorities(userAuthorities(user.getId()))
                .build();
    }

    private List<? extends GrantedAuthority> userAuthorities(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        List<GrantedAuthority> authorities = userRepository.findRolesByUserId(userId)
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
        authorities.addAll(AuthorityUtils.createAuthorityList(userRepository.findAuthoritiesByUserId(userId)));
        return authorities;
    }
}
