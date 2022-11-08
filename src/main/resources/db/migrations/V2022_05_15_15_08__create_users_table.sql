CREATE TABLE IF NOT EXISTS users (
    id varchar UNIQUE PRIMARY KEY NOT NULL,
    email varchar,
    "password" varchar,
    enabled boolean
);