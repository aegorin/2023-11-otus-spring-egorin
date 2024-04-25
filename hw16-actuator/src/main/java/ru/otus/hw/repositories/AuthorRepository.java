package ru.otus.hw.repositories;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.models.Author;

@RepositoryRestResource(path = "author")
public interface AuthorRepository extends ListCrudRepository<Author, Long> {
}