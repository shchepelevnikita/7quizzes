package it.sevenbits.quizzes.core.repositories.RoomRepository;

import it.sevenbits.quizzes.core.models.Player;
import it.sevenbits.quizzes.core.models.Room;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * room repository that works with postgres db
 */
public class PostgresRoomRepository implements IRoomRepository {
    private final JdbcOperations jdbcOperations;

    /**
     * constructor for postgres room repo
     *
     * @param jdbcOperations - jdbcoperations instance to initialize
     */
    public PostgresRoomRepository(final JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    /**
     * method to get all rooms
     *
     * @return - all rooms
     */
    @Override
    public List<Room> getRooms() {
        return jdbcOperations.query("SELECT \"name\", id, ownerid FROM rooms", (resultSet, i) ->
                new Room(resultSet.getString("name"), jdbcOperations.query(
                        "SELECT players_stats_per_game.playerid FROM players_stats_per_game WHERE players_stats_per_game.roomid = ?",
                        (playerResultSet, j) -> new Player(playerResultSet.getString("playerid")),
                        resultSet.getString("id")), resultSet.getString("ownerid"), resultSet.getString("id")));
    }

    /**
     * method to create a room
     *
     * @param roomName - room name
     * @param userId   - user id
     * @return - created room
     */
    @Override
    public Room createRoom(final String roomName, final String userId) {
        Room tempRoom = new Room(roomName, new ArrayList<>(), userId,
                UUID.randomUUID().toString());

        jdbcOperations.update("INSERT INTO rooms (id, \"name\", ownerId) VALUES (?, ?, ?)", tempRoom.getRoomId(),
                tempRoom.getRoomName(), userId);

        return tempRoom;
    }

    /**
     * method to get room by roomid
     *
     * @param roomId - id of the room to get
     * @return - room
     */
    @Override
    public Room getRoom(final String roomId) {
        return jdbcOperations.queryForObject("SELECT \"name\", id, ownerid FROM rooms WHERE id = ?", (resultSet, i) ->
                        new Room(resultSet.getString("name"), jdbcOperations.query("SELECT playerid FROM "
                                + "players_stats_per_game WHERE roomid = ?", (playerResultSet, j) ->
                                new Player(playerResultSet.getString("playerid")), roomId), resultSet.getString("ownerid"),
                                resultSet.getString("id")),
                roomId);
    }

    /**
     * method to add player to the room
     *
     * @param roomId - id of the room
     * @param userId - user id
     */
    @Override
    public void addPlayer(final String roomId, final String userId) {
        jdbcOperations.update("INSERT INTO players_stats_per_game (roomid, playerid, totalscore) VALUES (?, ?, ?)",
                roomId, userId, 0);
    }

    /**
     * method to remove player from the room
     * @param roomId - id of the room
     * @param userId - id of the user
     */
    @Override
    public void removePlayer(final String roomId, final String userId) {
        jdbcOperations.update("DELETE FROM players_stats_per_game WHERE players_stats_per_game.roomid = ? " +
                "AND players_stats_per_game.playerid = ?", roomId, userId);
    }
}