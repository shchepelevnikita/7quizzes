CREATE TABLE players_stats_per_question (
    playerId varchar,
    roomId varchar,
    questionId varchar,
    hasAnswered boolean,
    questionScore int,
    PRIMARY KEY (playerId, roomId, questionId)
);

ALTER TABLE players_stats_per_question ADD FOREIGN KEY (playerId) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE players_stats_per_question ADD FOREIGN KEY (questionId) REFERENCES questions (id) ON DELETE CASCADE;

ALTER TABLE players_stats_per_question ADD FOREIGN KEY (roomId) REFERENCES rooms (id) ON DELETE CASCADE;
