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
import ru.otus.hw.domain.Author;
import ru.otus.hw.domain.mongo.AuthorDocument;

import static ru.otus.hw.config.MigrationToNoSQLConfiguration.CHUNK_SIZE;

@RequiredArgsConstructor
@Component
public class AuthorsMigrationStep {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager transactionManager;

    @Bean
    public Step authorsStep(ItemReader<Author> authorItemReader,
                            ItemProcessor<Author, AuthorDocument> authorProcessor,
                            ItemWriter<AuthorDocument> authorDocumentWriter) {
        return new StepBuilder("authorsLoad", jobRepository)
                .<Author, AuthorDocument>chunk(CHUNK_SIZE, transactionManager)
                .reader(authorItemReader)
                .processor(authorProcessor)
                .writer(authorDocumentWriter)
                .build();
    }
}
