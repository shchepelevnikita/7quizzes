CREATE TABLE players_stats_per_game (
    playerId varchar,
    roomId varchar,
    totalScore int,
    PRIMARY KEY (playerId, roomId)
);

ALTER TABLE players_stats_per_game ADD FOREIGN KEY (playerId) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE players_stats_per_game ADD FOREIGN KEY (roomId) REFERENCES rooms (id) ON DELETE CASCADE;

