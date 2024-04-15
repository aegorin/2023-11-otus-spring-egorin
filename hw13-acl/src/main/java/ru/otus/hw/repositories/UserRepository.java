package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByLogin(String login);

    @Query(nativeQuery = true, value = """
            SELECT r.role_name
            FROM ss_group_members gm
            JOIN ss_group_roles gr ON gr.group_id = gm.group_id
            JOIN ss_roles r ON r.id = gr.role_id
            WHERE gm.user_id = :userId""")
    List<String> findRolesByUserId(long userId);

    @Query(nativeQuery = true, value = """
            SELECT a.authority
            FROM ss_group_members gm
            JOIN ss_authority_groups ag ON gm.group_id = ag.group_id
            JOIN ss_authorities a ON ag.authority_id = a.id
            WHERE gm.user_id = :userId
            UNION ALL
            SELECT a.authority
            FROM ss_authority_users sa
            JOIN ss_authorities a ON sa.authority_id = a.id
            WHERE sa.user_id = :userId""")
    List<String> findAuthoritiesByUserId(long userId);
}
