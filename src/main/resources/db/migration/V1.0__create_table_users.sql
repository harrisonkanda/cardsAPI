CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;

-- Create table users if does not exist

CREATE TABLE IF NOT EXISTS "users" (
    "user_id" UUID DEFAULT uuid_generate_v4() NOT NULL,
    "email" VARCHAR(50) NOT NULL,
    "password" VARCHAR(500) NOT NULL,
    "role" VARCHAR(8) NOT NULL,

    CONSTRAINT "PK_users_userId"  PRIMARY KEY ("user_id"),
    CONSTRAINT "UNQ_users_email" UNIQUE ("email")
);