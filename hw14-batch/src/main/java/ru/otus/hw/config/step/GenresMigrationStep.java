package ru.otus.hw.config.step;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.domain.Genre;
import ru.otus.hw.domain.mongo.GenreDocument;

import static ru.otus.hw.config.MigrationToNoSQLConfiguration.CHUNK_SIZE;

@RequiredArgsConstructor
@Component
public class GenresMigrationStep {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager transactionManager;

    @Bean
    public Step genresStep(ItemReader<Genre> genreItemReader,
                           ItemProcessor<Genre, GenreDocument> genreProcessor,
                           ItemWriter<GenreDocument> genreDocumentWriter) {
        return new StepBuilder("genresLoad", jobRepository)
                .<Genre, GenreDocument>chunk(CHUNK_SIZE, transactionManager)
                .reader(genreItemReader)
                .processor(genreProcessor)
                .writer(genreDocumentWriter)
                .build();
    }
}
