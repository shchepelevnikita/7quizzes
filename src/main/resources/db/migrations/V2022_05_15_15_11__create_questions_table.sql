CREATE TABLE IF NOT EXISTS questions (
    id varchar UNIQUE PRIMARY KEY NOT NULL,
    correctAnswerId varchar,
    "text" varchar,
    score int
);
