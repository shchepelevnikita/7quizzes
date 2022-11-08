package it.sevenbits.quizzes.core.exceptions;

/**
 * this exception occurs when the player tries to join the room he is already in
 */
public class PlayerAlreadyInsideRoomException extends RuntimeException {
    /**
     * exception constructor
     * @param message - message which contains the reason of exception
     */
    public PlayerAlreadyInsideRoomException(final String message) {
        super(message);
    }
}
