package it.sevenbits.quizzes.web.models;

/**
 * Request model containing playerId for startGame method (tells which player has started the game)
 */
public class StartGameRequest {

    private String playerId;

    /**
     * Empty constructor for json deserialization
     */
    public StartGameRequest() {}

    /**
     * Model constructor
     * @param playerId - id of the player
     */
    public StartGameRequest(final String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(final String playerId) {
        this.playerId = playerId;
    }

}
