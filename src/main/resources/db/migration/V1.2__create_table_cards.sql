CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;

-- Create table cards if does not exist

CREATE TABLE IF NOT EXISTS "cards" (
    "card_id" UUID DEFAULT uuid_generate_v4() NOT NULL,
    "name" VARCHAR(50) NOT NULL,
    "color" VARCHAR(20),
    "description" VARCHAR(200),
    "status" VARCHAR(200) NOT NULL,
    "date_created" timestamp without time zone,
    "owner" uuid NOT NULL,
    FOREIGN KEY ("owner") REFERENCES "users" ("user_id"),
    CONSTRAINT "PK_cards_cardId"  PRIMARY KEY ("card_id"),
    CONSTRAINT "UNQ_cards_name" UNIQUE ("name")
);