package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.LoginUser;

import java.util.Optional;

public interface LoginUserRepository extends CrudRepository<LoginUser, Long> {

    Optional<LoginUser> findByLogin(String login);
}
