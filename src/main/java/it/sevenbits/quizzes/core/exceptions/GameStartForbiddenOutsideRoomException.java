package it.sevenbits.quizzes.core.exceptions;

/**
 * this exception occurs when the player tries to start the game outside of the room
 */
public class GameStartForbiddenOutsideRoomException extends RuntimeException {
    /**
     * exception constructor
     * @param message - message which contains the reason of exception
     */
    public GameStartForbiddenOutsideRoomException(final String message) {
        super(message);
    }
}
