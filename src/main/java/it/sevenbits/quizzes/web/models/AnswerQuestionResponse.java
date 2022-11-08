package it.sevenbits.quizzes.web.models;

/**
 * Response that receives client from server after sending request of answering
 */
public class AnswerQuestionResponse {
    private final String correctAnswerId;
    private final String questionId;
    private final int totalScore;
    private final int questionScore;

    /**
     * Response contructor that inits:
     * @param correctAnswerId - id of correct answer
     * @param questionId - id of next question
     * @param totalScore - total score of the game
     * @param questionScore - question score (amount of points brought by answering question)
     */
    public AnswerQuestionResponse(final String correctAnswerId, final String questionId, final int totalScore,
                                  final int questionScore)  {
        this.correctAnswerId = correctAnswerId;
        this.questionId = questionId;
        this.totalScore = totalScore;
        this.questionScore = questionScore;
    }

    /**
     * Getter for correct answer id
     * @return - id of correct answer
     */
    public String getCorrectAnswerId() {
        return correctAnswerId;
    }

    /**
     * Getter for next question id
     * @return - next question id
     */
    public String getquestionId() {
        return questionId;
    }

    /**
     * Getter for total score
     * @return - total score of the game
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Getter for question score
     * @return - points that are received if the question is answered correctly
     */
    public int getQuestionScore() {
        return questionScore;
    }
}
