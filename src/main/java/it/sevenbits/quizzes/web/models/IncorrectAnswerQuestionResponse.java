package it.sevenbits.quizzes.web.models;

/**
 * Model for response in case of incorrect answer
 */
public class IncorrectAnswerQuestionResponse {
    private final String questionId;
    private final int totalScore;

    /**
     *
     * @param questionId - id of the question
     *                   (current question or if it's the last player answering - next question)
     * @param totalScore - total score of the player
     */
    public IncorrectAnswerQuestionResponse(final String questionId, final int totalScore) {
        this.questionId = questionId;
        this.totalScore = totalScore;
    }

    public String getQuestionId() {
        return questionId;
    }

    public int getTotalScore() {
        return totalScore;
    }
}
