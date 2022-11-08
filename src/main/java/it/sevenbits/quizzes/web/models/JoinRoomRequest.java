package it.sevenbits.quizzes.web.models;

/**
 * Request model containing playerId to join the room
 */
public class JoinRoomRequest {
    private String playerId;

    /**
     * Default constructor (is needed for json deserialization alongside with setters)
     */
    public JoinRoomRequest() {}

    /**
     * Constructor with playerid
     * @param playerId - playerid
     */
    public JoinRoomRequest(final String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(final String playerId) {
        this.playerId = playerId;
    }
}
