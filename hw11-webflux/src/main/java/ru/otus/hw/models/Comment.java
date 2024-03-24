package ru.otus.hw.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@NoArgsConstructor
@Getter
@Setter
@Table("comments")
public class Comment {

    @Id
    private Long id;

    private String text;

    @Column("book_id")
    private Long bookId;

    public Comment(String text, long bookId) {
        this.text = text;
        this.bookId = bookId;
    }
}
