--liquibase formatted sql

--changeset egorin.av:2024-01-02--02-insert-data
INSERT INTO authors(id, full_name) VALUES
(DEFAULT, 'Lewis Carroll'),
(DEFAULT, 'Alexandre Dumas'),
(DEFAULT, 'Leo Tolstoy');

INSERT INTO genres(id, name) VALUES
(DEFAULT, 'Fantasy'),
(DEFAULT, 'Adventure'),
(DEFAULT, 'Romance');

INSERT INTO books(id, title, author_id, genre_id) VALUES
(DEFAULT, 'Alice’s Adventures in Wonderland', 1, 1),
(DEFAULT, 'The Three Musketeers', 2, 2),
(DEFAULT, 'Anna Karenina', 3, 3);
