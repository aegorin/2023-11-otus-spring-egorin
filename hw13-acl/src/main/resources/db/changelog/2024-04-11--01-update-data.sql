--liquibase formatted sql

--changeset egorin.av:2024-04-11--01-update-data
INSERT INTO ss_roles(role_name) VALUES
('CUSTOMER');

INSERT INTO ss_group_roles(group_id, role_id)
SELECT g.id, r.id
FROM ss_groups g, ss_roles r
WHERE g.group_name IN ('MANAGERS', 'CUSTOMERS')
AND r.role_name = 'CUSTOMER';

DELETE FROM ss_authority_groups;
DELETE FROM ss_authority_users;
DELETE FROM ss_authorities;

ALTER TABLE books ADD COLUMN owner_id BIGINT CONSTRAINT books_owner_id_fkey REFERENCES users(id) ON DELETE SET NULL;
