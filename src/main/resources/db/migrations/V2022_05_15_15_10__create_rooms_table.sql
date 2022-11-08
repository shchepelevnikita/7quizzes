CREATE TABLE IF NOT EXISTS rooms (
    id varchar UNIQUE PRIMARY KEY NOT NULL,
    ownerId varchar,
    "name" varchar,
    hasStarted boolean,
    status varchar,
    questionId varchar,
    questionNumber int,
    questionsCount int
);
