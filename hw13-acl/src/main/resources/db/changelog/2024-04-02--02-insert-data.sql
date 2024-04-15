--liquibase formatted sql

--changeset egorin.av:2024-04-02--02-insert-data
INSERT INTO authors(id, full_name) VALUES
(DEFAULT, 'Льюис Кэрролл'),
(DEFAULT, 'Александр Дюма'),
(DEFAULT, 'Лев Толстой');

INSERT INTO genres(id, name) VALUES
(DEFAULT, 'Фантастика'),
(DEFAULT, 'Приключения'),
(DEFAULT, 'Роман');

INSERT INTO books(id, title, author_id, genre_id) VALUES
(DEFAULT, 'Алиса в стране чудес', 1, 1);
INSERT INTO comments(book_id, text)
SELECT 1, 'Сказка на все времена и поколения'
WHERE EXISTS(SELECT null FROM books WHERE id = 1);

INSERT INTO books(id, title, author_id, genre_id) VALUES
(DEFAULT, 'Три мушкетёра', 2, 2);
INSERT INTO comments(book_id, text)
SELECT 2, t.x
FROM (VALUES('"Три мушкетера" - это типичный исторический роман-фельетон. Но от этого он не перестает быть удивительным.'),
('3 мушкетера... Что может быть лучше четверки друзей готовых на все ради друг друга!'),
('Книга несравненно лучше всех экранизаций!')) AS t(x)
WHERE EXISTS(SELECT null FROM books WHERE id = 2);

INSERT INTO books(id, title, author_id, genre_id) VALUES
(DEFAULT, 'Анна Каренина', 3, 3);
INSERT INTO comments(book_id, text)
SELECT 3, t.x
FROM (VALUES('"Анну Каренину" читать надо минимум дважды! Первый раз следишь за сюжетом, знакомишься с героями, во второй раз уже обращаешь внимание на все детали, анализируешь каждого персонажа.'),
('Классика Мировая. Толстой... Что говорить?')) AS t(x)
WHERE EXISTS(SELECT null FROM books WHERE id = 3);

INSERT INTO ss_roles(role_name) VALUES
('USER'),
('MANAGER');

INSERT INTO ss_groups(group_name) VALUES
('USERS'),
('CUSTOMERS'),
('MANAGERS');

INSERT INTO ss_group_roles(group_id, role_id)
SELECT g.id, r.id
FROM ss_groups g, ss_roles r
WHERE r.role_name = 'USER';

INSERT INTO ss_group_roles(group_id, role_id)
SELECT g.id, r.id
FROM ss_groups g, ss_roles r
WHERE g.group_name = 'MANAGERS'
AND r.role_name = 'MANAGER';

INSERT INTO users(login, password) VALUES
('user', '$2a$10$BtvDB3ct/E.XERrHf3y7KOauYuJdUsroFPmetuMNe3uMfNh2G9Crq'),
('customer', '$2a$10$DzElEJ.nlehqzIxulJV2Qe5EX69psE/bx6DmwDgb.OgIMkiQlTqAa'),
('manager', '$2a$10$TOKDDl05WDBvzaVN1zCCI.g0WgxDBL6xeNEfUwPQiWIzu7rIszG7m');

INSERT INTO ss_group_members(group_id, user_id)
SELECT g.id, u.id
FROM ss_groups g, users u
WHERE
(g.group_name = 'MANAGERS' AND u.login = 'manager') OR
(g.group_name = 'CUSTOMERS' AND u.login = 'customer') OR
(g.group_name = 'USERS' AND u.login = 'user');

INSERT INTO ss_authorities(authority) VALUES
('BOOK_COMMENTS_VIEW'),
('BOOK_MODIFY'),
('BOOK_DELETE');

INSERT INTO ss_authority_groups(authority_id, group_id)
SELECT a.id, g.id
FROM ss_authorities a, ss_groups g
WHERE a.authority IN ('BOOK_COMMENTS_VIEW', 'BOOK_MODIFY')
AND g.group_name IN ('CUSTOMERS', 'MANAGERS');

INSERT INTO ss_authority_users(authority_id, user_id)
SELECT a.id, u.id
FROM ss_authorities a, users u
WHERE a.authority = 'BOOK_DELETE'
AND u.login = 'manager';
