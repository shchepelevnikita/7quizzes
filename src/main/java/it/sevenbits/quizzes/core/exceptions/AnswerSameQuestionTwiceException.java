package it.sevenbits.quizzes.core.exceptions;

/**
 * exception which occurs when the player answers the same question twice
 */
public class AnswerSameQuestionTwiceException extends RuntimeException {
    /**
     * exception constructor
     * @param message - message which contains the reason of exception
     */
    public AnswerSameQuestionTwiceException(final String message) {
        super(message);
    }
}
