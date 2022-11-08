CREATE TABLE players_stats_per_question_jsonb (
                                            playerId varchar,
                                            roomId varchar,
                                            questionId varchar,
                                            playerStatsPerQuestion jsonb,
                                            PRIMARY KEY (playerId, roomId, questionId)
);

ALTER TABLE players_stats_per_question_jsonb ADD FOREIGN KEY (playerId) REFERENCES users_jsonb (id) ON DELETE CASCADE;

ALTER TABLE players_stats_per_question_jsonb ADD FOREIGN KEY (questionId) REFERENCES questions_jsonb (id) ON DELETE CASCADE;

ALTER TABLE players_stats_per_question_jsonb ADD FOREIGN KEY (roomId) REFERENCES rooms_jsonb (id) ON DELETE CASCADE;
