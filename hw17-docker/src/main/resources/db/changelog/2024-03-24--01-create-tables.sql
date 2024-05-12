--liquibase formatted sql

--changeset egorin.av:2024-03-24--01-create-tables
CREATE TABLE authors (
  id BIGSERIAL,
  full_name VARCHAR(255) NOT NULL,
  CONSTRAINT author_pk PRIMARY KEY(id)
);

CREATE TABLE genres (
  id BIGSERIAL,
  name VARCHAR(255) NOT NULL,
  CONSTRAINT genre_pk PRIMARY KEY(id)
);

CREATE TABLE books (
  id BIGSERIAL,
  title VARCHAR(255) NOT NULL,
  author_id BIGINT NOT NULL,
  genre_id BIGINT NOT NULL,
  CONSTRAINT book_pk PRIMARY KEY(id),
  CONSTRAINT author_fk FOREIGN KEY(author_id) REFERENCES authors(id),
  CONSTRAINT genre_fk FOREIGN KEY(genre_id) REFERENCES genres(id)
);

CREATE INDEX author_fk_idx ON books(author_id);
CREATE INDEX genre_fk_idx ON books(genre_id);

CREATE TABLE comments (
  id BIGSERIAL,
  text VARCHAR(1024) NOT NULL,
  book_id BIGINT NOT NULL,
  CONSTRAINT book_comment_pk PRIMARY KEY(id),
  CONSTRAINT book_comment_fk FOREIGN KEY(book_id) REFERENCES books(id) ON DELETE CASCADE
);

CREATE INDEX book_comment_fk_idx ON comments(book_id);

CREATE TABLE user_tokens (
  series VARCHAR(64) not null,
  username VARCHAR(64) not null,
  token VARCHAR(64) not null,
  last_used TIMESTAMP not null,
  CONSTRAINT user_tokens_pk PRIMARY KEY(series)
);

CREATE INDEX user_tokens_username_idx ON user_tokens(username);

CREATE TABLE users (
  id BIGSERIAL not null,
  login VARCHAR(64) not null,
  password VARCHAR(256) not null,
  blocked BOOLEAN not null default false,
  CONSTRAINT users_pk PRIMARY KEY(id)
);

CREATE UNIQUE INDEX users_login_idx ON users(login);
