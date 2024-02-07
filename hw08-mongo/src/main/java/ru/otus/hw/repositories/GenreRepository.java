package ru.otus.hw.repositories;

import org.springframework.data.repository.ListCrudRepository;
import ru.otus.hw.models.Genre;

public interface GenreRepository extends ListCrudRepository<Genre, String> {
}
