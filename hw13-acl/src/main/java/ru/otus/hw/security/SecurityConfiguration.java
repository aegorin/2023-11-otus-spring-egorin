package ru.otus.hw.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import ru.otus.hw.config.SecurityProperties;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final SecurityProperties securityProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   RememberMeServices rememberMeServices) throws Exception {
            return httpSecurity
                    .authorizeHttpRequests(requests -> requests
                            .requestMatchers("/login").permitAll()
                            .requestMatchers(HttpMethod.POST, "/book").hasRole("CUSTOMER")

                            .requestMatchers(HttpMethod.PUT,"/book/{bookId:\\d+}")
                            .hasAnyRole("CUSTOMER", "MANAGER")

                            .requestMatchers(HttpMethod.DELETE,"/book/delete/{bookId:\\d+}")
                            .hasAnyRole("CUSTOMER", "MANAGER")
                            .requestMatchers(HttpMethod.GET).hasRole("USER")
                            .anyRequest().denyAll())
                    .formLogin(this::formLoginSettings)
                    .logout(this::logoutSetting)
                    .rememberMe(memberService -> memberService.rememberMeServices(rememberMeServices))
                    .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public RememberMeServices persistentTokenRememberService(UserDetailsService userDetailsService,
                                                             PersistentTokenRepository persistentTokenRepository) {
        var service = new PersistentTokenBasedRememberMeServices(securityProperties.persistentTokenKey(),
                userDetailsService, persistentTokenRepository);
        service.setTokenValiditySeconds(securityProperties.validTokenRememberMe());
        service.setCookieName(securityProperties.rememberMeCookie());
        return service;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2A, 10);
    }

    private void formLoginSettings(FormLoginConfigurer<HttpSecurity> form) {
        form.passwordParameter("a_pass")
                .usernameParameter("a_usr")
                .permitAll();
    }

    private void logoutSetting(LogoutConfigurer<HttpSecurity> logout) {
        logout.logoutUrl("/logout")
                .deleteCookies("JSESSIONID", securityProperties.rememberMeCookie())
                .permitAll();
    }
}
