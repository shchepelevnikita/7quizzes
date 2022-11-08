package it.sevenbits.quizzes.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request model for creating room
 */
public class CreateRoomRequest {
    private final String roomName;
    private final String playerId;

    /**
     * Model constructor
     * @param roomName - name of the room
     * @param playerId - id of the player
     */
    public CreateRoomRequest(@JsonProperty("roomName") final String roomName,
                             @JsonProperty("playerId") final String playerId) {
        this.roomName = roomName;
        this.playerId = playerId;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getPlayerId() {
        return playerId;
    }
}
