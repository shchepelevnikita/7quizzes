CREATE TABLE players_stats_per_game_jsonb (
                                        playerId varchar,
                                        roomId varchar,
                                        playerStatsPerGame jsonb,
                                        PRIMARY KEY (playerId, roomId)
);

ALTER TABLE players_stats_per_game_jsonb ADD FOREIGN KEY (playerId) REFERENCES users_jsonb (id) ON DELETE CASCADE;

ALTER TABLE players_stats_per_game_jsonb ADD FOREIGN KEY (roomId) REFERENCES rooms_jsonb (id) ON DELETE CASCADE;

