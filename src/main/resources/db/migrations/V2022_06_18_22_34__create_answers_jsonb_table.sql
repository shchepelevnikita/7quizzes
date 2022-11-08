CREATE TABLE answers_jsonb (
                         id varchar UNIQUE PRIMARY KEY NOT NULL,
                         questionId varchar,
                         answerData jsonb
);

ALTER TABLE answers_jsonb ADD FOREIGN KEY (questionId) REFERENCES questions_jsonb (id) ON DELETE CASCADE;
