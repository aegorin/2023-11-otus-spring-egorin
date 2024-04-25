package ru.otus.hw.models.projections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import ru.otus.hw.models.Author;

@Projection(name = "authorWithId", types = { Author.class })
public interface AuthorProjection {

    @Value("#{target.getId()}")
    Long getId();

    String getFullName();
}
