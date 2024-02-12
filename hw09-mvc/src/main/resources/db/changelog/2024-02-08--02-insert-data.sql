--liquibase formatted sql

--changeset egorin.av:2024-02-08--02-insert-data
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
