package it.sevenbits.quizzes.core.exceptions;

/**
 * exception which is a result of answering the already answered question
 */
public class QuestionAlreadyAnsweredByOtherPlayerException extends RuntimeException {
    /**
     * exception constructor
     * @param message - message which contains the reason of exception
     */
    public QuestionAlreadyAnsweredByOtherPlayerException(final String message) {
        super(message);
    }
}
