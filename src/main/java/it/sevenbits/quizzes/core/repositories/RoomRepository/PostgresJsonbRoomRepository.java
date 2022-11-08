package it.sevenbits.quizzes.core.repositories.RoomRepository;

import com.google.gson.Gson;
import it.sevenbits.quizzes.core.models.Player;
import it.sevenbits.quizzes.core.models.Room;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Postgres Room Repository that works with JSONB format
 */
public class PostgresJsonbRoomRepository implements IRoomRepository {
    private final JdbcOperations jdbcOperations;
    private final Gson gson;

    /**
     * Constructor of Postgres Room Repository that works with JSONB format
     * @param jdbcOperations instance
     */
    public PostgresJsonbRoomRepository(final JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
        this.gson = new Gson();
    }

    @Override
    public List<Room> getRooms() {
        return jdbcOperations.query("SELECT roomData->>'roomName', id, roomData->>'ownerId' FROM rooms_jsonb", (resultSet, i) ->
                new Room(resultSet.getString(1), jdbcOperations.query(
                        "SELECT players_stats_per_game_jsonb.playerid FROM players_stats_per_game_jsonb WHERE " +
                                "players_stats_per_game_jsonb.roomid = ?",
                        (playerResultSet, j) -> new Player(playerResultSet.getString(1)),
                        resultSet.getString(2)), resultSet.getString(3), resultSet.getString(2)));
    }

    @Override
    public Room createRoom(final String roomName, final String userId) {
        Room tempRoom = new Room(roomName, new ArrayList<>(), userId,
                UUID.randomUUID().toString());

        jdbcOperations.update("INSERT INTO rooms_jsonb (id, roomData) VALUES (?, ?::jsonb)", tempRoom.getRoomId(),
                gson.toJson(tempRoom));

        return tempRoom;
    }

    @Override
    public Room getRoom(final String roomId) {
        return jdbcOperations.queryForObject("SELECT roomData->>'roomName', id, roomData->>'ownerId' FROM " +
                        "rooms_jsonb WHERE id = ?", (resultSet, i) ->
                        new Room(resultSet.getString(1), jdbcOperations.query("SELECT playerid FROM "
                                + "players_stats_per_game_jsonb WHERE roomid = ?", (playerResultSet, j) ->
                                new Player(playerResultSet.getString(1)), roomId), resultSet.getString(3),
                                resultSet.getString(2)),
                roomId);
    }

    @Override
    public void addPlayer(final String roomId, final String userId) {
        jdbcOperations.update("INSERT INTO players_stats_per_game_jsonb (roomid, playerid, playerStatsPerGame) " +
                        "VALUES (?, ?, ?::jsonb)",
                roomId, userId, "{\"totalScore\": 0}");
    }

    /**
     * method to remove player from the room
     * @param roomId - id of the room
     * @param userId - id of the user
     */
    @Override
    public void removePlayer(final String roomId, final String userId) {
        jdbcOperations.update("DELETE FROM players_stats_per_game_jsonb WHERE players_stats_per_game_jsonb.roomid " +
                "= ? AND players_stats_per_game_jsonb.playerid = ?", roomId, userId);
    }
}
