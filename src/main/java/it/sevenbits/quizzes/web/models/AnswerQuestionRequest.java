package it.sevenbits.quizzes.web.models;

/**
 * DTO that is sent from client to server that contains client's answer
 */
public class AnswerQuestionRequest {
    private String answerId;
    private String playerId;

    /**
     * Empty constructor for json deserialization
     */
    public AnswerQuestionRequest() {}

    /**
     * Constructor with answer and player ids
     * @param answerId - answer id
     * @param playerId - player id
     */
    public AnswerQuestionRequest(final String answerId, final String playerId) {
        this.answerId = answerId;
        this.playerId = playerId;
    }

    /**
     * Gets answer id
     * @return - answer id
     */
    public String getAnswerId() {
        return answerId;
    }

    public String getPlayerId() {
        return playerId;
    }
}
