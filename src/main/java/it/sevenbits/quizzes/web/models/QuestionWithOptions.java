package it.sevenbits.quizzes.web.models;

import it.sevenbits.quizzes.core.models.QuestionAnswer;

import java.util.List;

/**
 * Question that is received by client, containing its id, text and answer options
 */
public class QuestionWithOptions {
    private final String questionId;
    private final String questionText;
    private final List<QuestionAnswer> answersList;

    /**
     * QuestionWithOptions contructor that receives questionId, questionText, answersList to init
     * @param questionId - id of received question
     * @param questionText - text of received question
     * @param answersList - list of answers to answer received question
     */
    public QuestionWithOptions(final String questionId, final String questionText,
                               final List<QuestionAnswer> answersList) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.answersList = answersList;
    }

    /**
     * Getter of questionId
     * @return - questionId
     */
    public String getQuestionId() {
        return questionId;
    }

    /**
     * Getter of questionText
     * @return - questionText
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     * Getter of answerList
     * @return - answerList
     */
    public List<QuestionAnswer> getAnswersList() {
        return answersList;
    }
}
