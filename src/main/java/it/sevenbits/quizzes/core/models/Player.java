package it.sevenbits.quizzes.core.models;

/**
 * Model for player
 */
public class Player {
    private final String playerId;

    /**
     * Constructor for model
     * @param playerId - id of the player
     */
    public Player(final String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }
}
