CREATE TABLE IF NOT EXISTS questions_jsonb (
                                         id varchar UNIQUE PRIMARY KEY NOT NULL,
                                         questionData jsonb
);
