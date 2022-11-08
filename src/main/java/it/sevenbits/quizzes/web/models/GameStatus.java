package it.sevenbits.quizzes.web.models;

/**
 * Model representing game status and containing the status itself, current question id, total number questions
 * and amount of questions that were given to players to answer
 */
public class GameStatus {
    private String status;
    private String questionId;
    private int questionNumber;
    private int questionsCount;

    /**
     * Default constructor for json deserialization
     */
    public GameStatus() {}

    /**
     * Constructor which initializes all game status parameters
     * @param status - status of the game e.g. has not started, in process, finished
     * @param questionId - id of the current in-game question
     * @param questionNumber - total number of questions
     * @param questionsCount - amount of questions that were passed to players
     */
    public GameStatus(final String status, final String questionId, final int questionNumber, final int questionsCount) {
        this.status = status;
        this.questionId = questionId;
        this.questionNumber = questionNumber;
        this.questionsCount = questionsCount;
    }

    public String getStatus() {
        return status;
    }

    public String getQuestionId() {
        return questionId;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public int getQuestionsCount() {
        return questionsCount;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public void setQuestionId(final String questionId) {
        this.questionId = questionId;
    }

    public void setQuestionNumber(final int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public void setQuestionsCount(final int questionsCount) {
        this.questionsCount = questionsCount;
    }
}
