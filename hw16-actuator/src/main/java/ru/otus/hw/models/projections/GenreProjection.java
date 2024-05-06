package ru.otus.hw.models.projections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import ru.otus.hw.models.Genre;

@Projection(name = "genreWithId", types = { Genre.class })
public interface GenreProjection {

    @Value("#{target.getId()}")
    Long getId();

    String getName();
}
