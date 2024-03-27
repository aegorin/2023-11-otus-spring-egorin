--liquibase formatted sql

--changeset egorin.av:2024-03-24--03-insert-data-test
INSERT INTO authors(id, full_name) VALUES
(DEFAULT, 'Author_1'),
(DEFAULT, 'Author_2'),
(DEFAULT, 'Author_3');

INSERT INTO genres(id, name) VALUES
(DEFAULT, 'Genre_1'),
(DEFAULT, 'Genre_2'),
(DEFAULT, 'Genre_3');

INSERT INTO books(id, title, author_id, genre_id) VALUES
(DEFAULT, 'Book_1', 1, 1),
(DEFAULT, 'Book_2', 2, 2),
(DEFAULT, 'Book_3', 3, 3);

INSERT INTO comments(book_id, text)
VALUES
(1, 'Comment_1_book_1'),
(1, 'Comment_2_book_1'),
(2, 'Comment_1_book_2');
