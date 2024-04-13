package ru.otus.hw.service;

import org.bson.types.ObjectId;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.otus.hw.cache.EntityMongoMap;
import ru.otus.hw.domain.Author;
import ru.otus.hw.domain.Book;
import ru.otus.hw.domain.Comment;
import ru.otus.hw.domain.Genre;
import ru.otus.hw.domain.mongo.AuthorDocument;
import ru.otus.hw.domain.mongo.BookDocument;
import ru.otus.hw.domain.mongo.CommentDocument;
import ru.otus.hw.domain.mongo.GenreDocument;

import java.util.Optional;

@Component
public class RelationalEntityProcessor {

    private final EntityMongoMap<AuthorDocument> authorMongoMap = new EntityMongoMap<>();

    private final EntityMongoMap<GenreDocument> genreMongoMap = new EntityMongoMap<>();

    private final EntityMongoMap<BookDocument> bookMongoMap = new EntityMongoMap<>();

    @Bean
    public ItemProcessor<Author, AuthorDocument> authorProcessor() {
        return authorItem -> {
            var authorDocument = new AuthorDocument(ObjectId.get().toString(), authorItem.getFullName());
            authorMongoMap.put(authorItem.getId(), authorDocument);
            return authorDocument;
        };
    }

    @Bean
    public ItemProcessor<Genre, GenreDocument> genreProcessor() {
        return genreItem -> {
            var genreDocument = new GenreDocument(ObjectId.get().toString(), genreItem.getName());
            genreMongoMap.put(genreItem.getId(), genreDocument);
            return genreDocument;
        };
    }

    @Bean
    public ItemProcessor<Book, BookDocument> bookProcessor() {
        return bookItem -> {
            var genre = Optional.ofNullable(bookItem.getGenre())
                    .map(g -> genreMongoMap.get(g.getId()))
                    .orElse(null);
            var author = Optional.ofNullable(bookItem.getAuthor())
                    .map(a -> authorMongoMap.get(a.getId()))
                    .orElse(null);
            var bookDocument = new BookDocument(ObjectId.get().toString(), bookItem.getTitle(), author, genre);
            bookMongoMap.put(bookItem.getId(), bookDocument);
            return bookDocument;
        };
    }

    @Bean
    public ItemProcessor<Comment, CommentDocument> commentProcessor() {
        return commentItem -> {
            var book = Optional.ofNullable(commentItem.getBook())
                    .map(b -> bookMongoMap.get(b.getId()))
                    .orElse(null);
            return new CommentDocument(book, commentItem.getText());
        };
    }
}
