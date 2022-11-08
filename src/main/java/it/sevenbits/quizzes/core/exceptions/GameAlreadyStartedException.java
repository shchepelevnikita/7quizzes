package it.sevenbits.quizzes.core.exceptions;

/**
 * exception which occurs when the player starts the already started game
 */
public class GameAlreadyStartedException extends RuntimeException {
    /**
     * exception constructor
     * @param message - message which contains the reason of exception
     */
    public GameAlreadyStartedException(final String message) {
        super(message);
    }
}
