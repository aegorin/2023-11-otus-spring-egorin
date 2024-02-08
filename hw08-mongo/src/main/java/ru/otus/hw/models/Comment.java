package ru.otus.hw.models;

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
public class Comment {
    @Id
    private String id;

    @Field
    private String text;

    @DBRef(lazy = true)
    private Book book;

    public Comment(Book book) {
        this.book = book;
    }
}
