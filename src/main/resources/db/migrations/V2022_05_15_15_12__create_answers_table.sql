CREATE TABLE answers (
    id varchar UNIQUE PRIMARY KEY NOT NULL,
    questionId varchar,
    "text" varchar
);

ALTER TABLE answers ADD FOREIGN KEY (questionId) REFERENCES questions (id) ON DELETE CASCADE;
