package it.sevenbits.quizzes.core.models;

import java.util.List;

/**
 * DTO that contains every info about question: correct answer, its id and text, its value measured in score and list of possible answers
 */
public class Question {
    private final String correctAnswerId;
    private final String questionId;
    private final String questionText;
    private final List<QuestionAnswer> answersList;
    private final int questionScore;

    /**
     * Question constructor
     * @param correctAnswerId - id of correct answer
     * @param questionText - text of question
     * @param answersList - list of possible answers
     * @param questionScore - value of question (how many points it gives)
     * @param questionId - id of the question
     */
    public Question(final String correctAnswerId, final String questionText,
                    final List<QuestionAnswer> answersList, final int questionScore, final String questionId) {
        this.correctAnswerId = correctAnswerId;
        this.questionScore = questionScore;
        this.questionId = questionId;
        this.questionText = questionText;
        this.answersList = answersList;
    }

    /**
     * Getter of correct answer id
     * @return - id of correct answer
     */
    public String getCorrectAnswerId() {
        return correctAnswerId;
    }

    /**
     * Getter for questionId
     * @return - questionId
     */
    public String getQuestionId() {
        return questionId;
    }

    /**
     * Getter for questionText
     * @return - questionText
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     * Getter for list of possible answers
     * @return - list of possible answers
     */
    public List<QuestionAnswer> getAnswersList() {
        return answersList;
    }

    /**
     * Getter for question score
     * @return - questionScore
     */
    public int getQuestionScore() {
        return questionScore;
    }
}
