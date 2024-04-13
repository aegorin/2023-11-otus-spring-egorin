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
import ru.otus.hw.domain.Comment;
import ru.otus.hw.domain.mongo.CommentDocument;

import static ru.otus.hw.config.MigrationToNoSQLConfiguration.CHUNK_SIZE;

@RequiredArgsConstructor
@Component
public class CommentsMigrationStep {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager transactionManager;

    @Bean
    public Step commentsStep(ItemReader<Comment> commentItemReader,
                             ItemProcessor<Comment, CommentDocument> commentProcessor,
                             ItemWriter<CommentDocument> commentDocumentWriter) {
        return new StepBuilder("commentsLoad", jobRepository)
                .<Comment, CommentDocument>chunk(CHUNK_SIZE, transactionManager)
                .reader(commentItemReader)
                .processor(commentProcessor)
                .writer(commentDocumentWriter)
                .build();
    }
}
