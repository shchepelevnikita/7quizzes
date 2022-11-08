package it.sevenbits.quizzes.core.exceptions;

/**
 * this exception occurs when the player tries to exit the room before he joins it
 */
public class PlayerExitRoomBeforeJoinException extends RuntimeException {
    /**
     * exception constructor
     * @param message - message which contains the reason of exception
     */
    public PlayerExitRoomBeforeJoinException(final String message) {
        super(message);
    }
}
