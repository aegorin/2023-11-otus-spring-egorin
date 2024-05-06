package ru.otus.hw.models.projections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import ru.otus.hw.models.Book;

@Projection(name = "withAuthorGenre", types = { Book.class })
public interface BookWithAuthorAndGenre {

    @Value("#{target.getId()}")
    Long getId();

    String getTitle();

    AuthorProjection getAuthor();

    GenreProjection getGenre();
}
