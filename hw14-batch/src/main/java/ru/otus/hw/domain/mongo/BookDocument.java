package ru.otus.hw.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("books")
public class BookDocument {
    @Id
    private String id;

    @Field
    private String title;

    @Field
    private AuthorDocument author;

    @Field
    private GenreDocument genre;
}
