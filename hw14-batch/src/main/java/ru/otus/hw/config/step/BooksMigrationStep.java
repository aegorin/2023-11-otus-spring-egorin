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
import ru.otus.hw.domain.Book;
import ru.otus.hw.domain.mongo.BookDocument;

import static ru.otus.hw.config.MigrationToNoSQLConfiguration.CHUNK_SIZE;

@RequiredArgsConstructor
@Component
public class BooksMigrationStep {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager transactionManager;

    @Bean
    public Step booksStep(ItemReader<Book> bookItemReader,
                           ItemProcessor<Book, BookDocument> bookProcessor,
                           ItemWriter<BookDocument> bookDocumentWriter) {
        return new StepBuilder("booksLoad", jobRepository)
                .<Book, BookDocument>chunk(CHUNK_SIZE, transactionManager)
                .reader(bookItemReader)
                .processor(bookProcessor)
                .writer(bookDocumentWriter)
                .build();
    }
}
