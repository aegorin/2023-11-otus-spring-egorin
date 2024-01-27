--liquibase formatted sql

--changeset egorin.av:2024-01-27--01-create-book_comment_table
CREATE TABLE book_comments (
  id BIGINT AUTO_INCREMENT,
  comment_text VARCHAR(1024) NOT NULL,
  book_id BIGINT NOT NULL,
  CONSTRAINT book_comment_pk PRIMARY KEY(id),
  CONSTRAINT book_comment_fk FOREIGN KEY(book_id) REFERENCES books(id) ON DELETE CASCADE
);
