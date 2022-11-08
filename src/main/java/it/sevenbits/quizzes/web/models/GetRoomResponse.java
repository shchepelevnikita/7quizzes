package it.sevenbits.quizzes.web.models;

import it.sevenbits.quizzes.core.models.Player;

import java.util.List;

/**
 * Response model of getting a single room
 */
public class GetRoomResponse {
    private final String roomId;
    private final String roomName;
    private final String ownerId;
    private final List<Player> players;

    /**
     * Model constructor
     * @param roomId - id of the room
     * @param roomName - name of the room
     * @param ownerId - id of the room's owner
     * @param players - list of players inside the room
     */
    public GetRoomResponse(final String roomId, final String roomName, final String ownerId, final List<Player> players) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.players = players;
        this.ownerId = ownerId;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
