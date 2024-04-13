package ru.otus.hw.service;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.otus.hw.domain.Author;
import ru.otus.hw.domain.Book;
import ru.otus.hw.domain.Comment;
import ru.otus.hw.domain.Genre;

import static ru.otus.hw.config.MigrationToNoSQLConfiguration.MAXIMUM_READ_COUNT;

@RequiredArgsConstructor
@Component
public class RelationalEntityItemReader {


    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public ItemReader<Author> authorItemReader() {
        return new JpaCursorItemReaderBuilder<Author>()
                .name("authorItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("FROM Author")
                .maxItemCount(MAXIMUM_READ_COUNT)
                .build();
    }

    @Bean
    public ItemReader<Genre> genreItemReader() {
        return new JpaCursorItemReaderBuilder<Genre>()
                .name("genreItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("FROM Genre")
                .maxItemCount(MAXIMUM_READ_COUNT)
                .build();
    }

    @Bean
    public ItemReader<Book> bookItemReader() {
        return new JpaCursorItemReaderBuilder<Book>()
                .name("bookItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("FROM Book")
                .maxItemCount(MAXIMUM_READ_COUNT)
                .build();
    }

    @Bean
    public ItemReader<Comment> commentItemReader() {
        return new JpaCursorItemReaderBuilder<Comment>()
                .name("commentItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("FROM Comment")
                .maxItemCount(MAXIMUM_READ_COUNT)
                .build();
    }
}
