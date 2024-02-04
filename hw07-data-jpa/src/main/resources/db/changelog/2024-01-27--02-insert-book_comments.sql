--liquibase formatted sql

--changeset egorin.av:2024-01-27--02-insert-book_comments
INSERT INTO book_comments(book_id, comment_text)
SELECT 1, 'Is a quick and easy read'
WHERE EXISTS(SELECT null FROM books WHERE id = 1);

INSERT INTO book_comments(book_id, comment_text)
SELECT 2, t.x
FROM (VALUES('One for all and all for one'),
('The first part of the Merleçon Ballet!'),
('The second part of the Merleçon Ballet!')) AS t(x)
WHERE EXISTS(SELECT null FROM books WHERE id = 2);

INSERT INTO book_comments(book_id, comment_text)
SELECT 3, t.x
FROM (VALUES('Rightfully a classic. Outstanding character depictions.'),
('Amazing book......')) AS t(x)
WHERE EXISTS(SELECT null FROM books WHERE id = 3);
