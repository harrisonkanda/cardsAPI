

-- Insert test admin user & Member User into the "users" table
INSERT INTO "users" ("email", "password", "role")
VALUES
    ('harrison.kanda@logicea.com', '$2a$12$PBSUv8oauj2BqlMrIz./7O6FaCkxFFpVEWEl6JaUQC5BfagiaqWRi', 'ADMIN'),
    ('harrison.kiprono@logicea.com', '$2a$12$PBSUv8oauj2BqlMrIz./7O6FaCkxFFpVEWEl6JaUQC5BfagiaqWRi', 'MEMBER');