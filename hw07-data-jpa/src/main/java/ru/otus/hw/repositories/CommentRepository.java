package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    @Query("SELECT c FROM #{#entityName} c WHERE c.book.id = :bookId")
    List<Comment> findByBookId(Long bookId);
}
