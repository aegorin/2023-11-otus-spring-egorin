package ru.otus.hw.domain.mongo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@Document("book_comments")
public class CommentDocument {
    @Id
    private String id;

    @Field
    private String text;

    @DBRef(lazy = true)
    private BookDocument book;

    public CommentDocument(BookDocument book, String text) {
        this.book = book;
        this.text = text;
    }
}
