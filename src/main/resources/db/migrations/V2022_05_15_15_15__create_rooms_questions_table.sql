CREATE TABLE rooms_questions (
    roomId varchar,
    questionId varchar,
    isAnswered boolean,
    PRIMARY KEY (roomId, questionId)
);

ALTER TABLE rooms_questions ADD FOREIGN KEY (roomId) REFERENCES rooms (id) ON DELETE CASCADE;

ALTER TABLE rooms_questions ADD FOREIGN KEY (questionId) REFERENCES questions (id) ON DELETE CASCADE;
