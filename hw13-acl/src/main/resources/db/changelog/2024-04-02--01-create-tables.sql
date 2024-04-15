--liquibase formatted sql

--changeset egorin.av:2024-04-02--01-create-tables
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
CREATE UNIQUE INDEX users_login_unique ON users(login);

CREATE TABLE ss_roles (
  id BIGSERIAL not null,
  role_name VARCHAR(64) not null,
  CONSTRAINT ss_roles_pk PRIMARY KEY(id)
);
CREATE UNIQUE INDEX ss_roles_role_name_unique ON ss_roles(role_name);

CREATE TABLE ss_groups (
  id BIGSERIAL not null,
  group_name VARCHAR(64) not null,
  CONSTRAINT ss_groups_pk PRIMARY KEY(id)
);
CREATE UNIQUE INDEX ss_groups_group_name_unique ON ss_groups(group_name);

CREATE TABLE ss_group_roles (
  group_id BIGINT not null CONSTRAINT ss_group_roles_group_fkey REFERENCES ss_groups(id),
  role_id BIGINT not null CONSTRAINT ss_group_roles_role_fkey REFERENCES ss_roles(id)
);
CREATE UNIQUE INDEX ss_group_roles_group_role_unique ON ss_group_roles(group_id, role_id);

CREATE TABLE ss_group_members (
  group_id BIGINT not null CONSTRAINT ss_group_members_group_fkey REFERENCES ss_groups(id),
  user_id BIGINT not null CONSTRAINT ss_group_members_user_fkey REFERENCES users(id)
);
CREATE UNIQUE INDEX ss_group_members_user_group_unique ON ss_group_members(group_id, user_id);

CREATE TABLE ss_authorities (
  id BIGSERIAL not null,
  authority VARCHAR(64) not null,
  CONSTRAINT ss_authorities_pk PRIMARY KEY(id)
);
CREATE UNIQUE INDEX ss_authorities_authority_unique ON ss_authorities(authority);

CREATE TABLE ss_authority_groups (
  authority_id BIGINT not null CONSTRAINT ss_authority_groups_authority_fkey REFERENCES ss_authorities(id),
  group_id BIGINT not null CONSTRAINT ss_authority_groups_group_fkey REFERENCES ss_groups(id)
);
CREATE UNIQUE INDEX ss_authority_groups_unique ON ss_authority_groups(authority_id, group_id);

CREATE TABLE ss_authority_users (
  authority_id BIGINT not null CONSTRAINT ss_authority_users_authority_fkey REFERENCES ss_authorities(id),
  user_id BIGINT not null CONSTRAINT ss_authority_users_user_fkey REFERENCES users(id)
);
CREATE UNIQUE INDEX ss_authority_users_unique ON ss_authority_users(authority_id, user_id);
