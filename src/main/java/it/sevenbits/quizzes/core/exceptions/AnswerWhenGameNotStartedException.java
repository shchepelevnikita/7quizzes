package it.sevenbits.quizzes.core.exceptions;

/**
 * exception which occurs when the player answers the question before the game even started
 */
public class AnswerWhenGameNotStartedException extends RuntimeException {
    /**
     * exception constructor
     * @param message - message which contains the reason of exception
     */
    public AnswerWhenGameNotStartedException(final String message) {
        super(message);
    }
}
