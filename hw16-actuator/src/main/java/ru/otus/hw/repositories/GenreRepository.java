package ru.otus.hw.repositories;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.models.Genre;

@RepositoryRestResource(path = "genre", collectionResourceRel = "genres")
public interface GenreRepository extends ListCrudRepository<Genre, Long> {
}
