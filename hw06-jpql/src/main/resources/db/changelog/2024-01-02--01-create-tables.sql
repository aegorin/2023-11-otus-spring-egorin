--liquibase formatted sql

--changeset egorin.av:2024-01-02--01-create-tables
CREATE TABLE authors (
  id BIGINT AUTO_INCREMENT,
  full_name VARCHAR(255) NOT NULL,
  CONSTRAINT author_pk PRIMARY KEY(id)
);

CREATE TABLE genres (
  id BIGINT AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  CONSTRAINT genre_pk PRIMARY KEY(id)
);

CREATE TABLE books (
  id BIGINT AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  author_id BIGINT NOT NULL,
  genre_id BIGINT NOT NULL,
  CONSTRAINT book_pk PRIMARY KEY(id),
  CONSTRAINT author_fk FOREIGN KEY(author_id) REFERENCES authors(id),
  CONSTRAINT genre_fk FOREIGN KEY(genre_id) REFERENCES genres(id)
);
