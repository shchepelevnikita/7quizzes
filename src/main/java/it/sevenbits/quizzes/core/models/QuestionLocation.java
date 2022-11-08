package it.sevenbits.quizzes.core.models;

/**
 * DTO that only contains questionId
 */
public class QuestionLocation {
    private String questionId;

    /**
     * Default constructor
     */
    public QuestionLocation() {}

    /**
     * QuestionLocation constructor
     * @param questionId - id of the question
     */
    public QuestionLocation(final String questionId) {
        this.questionId = questionId;
    }

    /**
     * Getter for questionId
     * @return - questionId
     */
    public String getQuestionId() {
        return questionId;
    }
}
