package it.sevenbits.quizzes.core.repositories.QuestionRepository;

import it.sevenbits.quizzes.core.models.Question;

import java.util.List;

/**
 * Interface for QuestionRepository as there will be more implemented
 */
public interface IQuestionRepository {
    /**
     * Getter for question by id
     * @param questionId - questionId
     * @return - question containing questionId, rightAnswer, answersList, questionText and its value
     */
    Question getQuestion(String questionId);

    /**
     * Getter for random question id
     * @param randomQuestionIdList - list of random question ids
     * @return - random question id
     */
    String getRandomQuestionId(List<String> randomQuestionIdList);
}
