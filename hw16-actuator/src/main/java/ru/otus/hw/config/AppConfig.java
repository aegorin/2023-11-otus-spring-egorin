package ru.otus.hw.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import ru.otus.hw.services.RequestCountingService;

@Configuration
public class AppConfig {

    @Bean
    public FilterRegistrationBean<LogHttpRequestFilter> filterRegistrationBean(
            RequestCountingService requestCountingService) {
        var registrationBean = new FilterRegistrationBean<LogHttpRequestFilter>();
        registrationBean.setFilter(new LogHttpRequestFilter(requestCountingService));
        registrationBean.addUrlPatterns("/api/v1/*");
        return registrationBean;
    }

    @Bean
    public RepositoryRestConfigurer repositoryRestConfigurer() {
        return new RepositoryRestConfigurer() {
            @Override
            public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
                config.setReturnBodyOnDelete(false);
            }
        };
    }
}
