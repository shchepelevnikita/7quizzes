CREATE TABLE rooms_questions_jsonb (
                                 roomId varchar,
                                 questionId varchar,
                                 roomsQuestionsData jsonb,
                                 PRIMARY KEY (roomId, questionId)
);

ALTER TABLE rooms_questions_jsonb ADD FOREIGN KEY (roomId) REFERENCES rooms_jsonb (id) ON DELETE CASCADE;

ALTER TABLE rooms_questions_jsonb ADD FOREIGN KEY (questionId) REFERENCES questions_jsonb (id) ON DELETE CASCADE;
