package it.sevenbits.quizzes.web.models;

/**
 * Response model for getting total scores of all the players in a certain room
 */
public class GetTotalScoresResponse {
    private String playerId;
    private String playerName;
    private int totalScore;

    /**
     * Empty constructor for deserialization
     */
    public GetTotalScoresResponse() {}

    /**
     * Model's constructor
     * @param playerId - id of the player
     * @param playerName - name of the player
     * @param totalScore - player's total score
     */
    public GetTotalScoresResponse(final String playerId, final String playerName, final int totalScore) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.totalScore = totalScore;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(final String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(final String playerName) {
        this.playerName = playerName;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(final int totalScore) {
        this.totalScore = totalScore;
    }
}
