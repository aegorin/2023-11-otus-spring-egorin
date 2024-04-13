package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import ru.otus.hw.domain.mongo.AuthorDocument;
import ru.otus.hw.domain.mongo.BookDocument;
import ru.otus.hw.domain.mongo.CommentDocument;
import ru.otus.hw.domain.mongo.GenreDocument;

@RequiredArgsConstructor
@Component
public class MongoEntityItemWriter {

    private final MongoOperations mongoOperations;

    @Bean
    public ItemWriter<AuthorDocument> authorDocumentWriter() {
        return new MongoItemWriterBuilder<AuthorDocument>()
                .template(mongoOperations)
                .collection("authors")
                .build();
    }

    @Bean
    public ItemWriter<GenreDocument> genreDocumentWriter() {
        return new MongoItemWriterBuilder<GenreDocument>()
                .template(mongoOperations)
                .collection("genres")
                .build();
    }

    @Bean
    public ItemWriter<BookDocument> bookDocumentWriter() {
        return new MongoItemWriterBuilder<BookDocument>()
                .template(mongoOperations)
                .collection("books")
                .build();
    }

    @Bean
    public ItemWriter<CommentDocument> commentDocumentWriter() {
        return new MongoItemWriterBuilder<CommentDocument>()
                .template(mongoOperations)
                .collection("book_comments")
                .build();
    }
}
