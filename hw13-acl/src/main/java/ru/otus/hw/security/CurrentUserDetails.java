package ru.otus.hw.security;

import lombok.Getter;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CurrentUserDetails implements UserDetails, CredentialsContainer {

    @Getter
    private final Long userId;

    private final UserDetails delegate;

    private String password;

    public CurrentUserDetails(Long userId, UserDetails userDetails) {
        this.userId = userId;
        this.delegate = userDetails;
        this.password = userDetails.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return delegate.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return delegate.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return delegate.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return delegate.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return delegate.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return delegate.isEnabled();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CurrentUserDetails user) {
            return userId != null && userId.equals(user.getUserId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserId=" + userId + ", " + delegate.toString();
    }

    @Override
    public void eraseCredentials() {
        password = null;
        if (delegate instanceof CredentialsContainer cred) {
            cred.eraseCredentials();
        }
    }
}
