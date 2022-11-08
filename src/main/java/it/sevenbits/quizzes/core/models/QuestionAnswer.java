package it.sevenbits.quizzes.core.models;

/**
 * DTO that represents answers (its id and text)
 */
public class QuestionAnswer {
    private final String answerId;
    private final String answerText;

    /**
     * QuestionAnswer constructor that receives text for answer and generates its unique id
     * @param answerText - text of the answer
     * @param answerId - id of the answer
     */
    public QuestionAnswer(final String answerText, final String answerId) {
        this.answerId = answerId;
        this.answerText = answerText;
    }

    /**
     * Getter for answer id
     * @return - answer id
     */
    public String getAnswerId() {
        return answerId;
    }

    /**
     * Getter for answer text
     * @return - answer text
     */
    public String getAnswerText() {
        return answerText;
    }
}
