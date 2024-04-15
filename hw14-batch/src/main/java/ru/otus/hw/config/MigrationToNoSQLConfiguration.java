package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class MigrationToNoSQLConfiguration {

    public static final int MAXIMUM_READ_COUNT = 50;

    public static final int CHUNK_SIZE = 10;

    private final JobRepository jobRepository;

    private final Step authorsStep;

    private final Step genresStep;

    private final Step booksStep;

    private final Step commentsStep;

    @Bean
    public Job migrateToNoSqlJob() {
        return new JobBuilder("migrateToNoSqlJob", jobRepository)
                .start(authorsStep)
                .next(genresStep)
                .next(booksStep)
                .next(commentsStep)
                .build();
    }
}
