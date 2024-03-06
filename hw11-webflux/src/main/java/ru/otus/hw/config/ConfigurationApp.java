package ru.otus.hw.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationApp {

    @Bean
    @SuppressWarnings("unused")
    public ApplicationRunner emptyApplicationRunnerOverrideLiquibaseR2dbcUpdate() {
        return (args) -> {

        };
    }
}
