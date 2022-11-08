package it.sevenbits.quizzes.core.exceptions;

/**
 * exception which is a result of adding the user that already exists in the db
 */
public class UserAlreadyExistsException extends RuntimeException {
    /**
     * exception constructor
     * @param message - message which contains the reason of exception
     */
    public UserAlreadyExistsException(final String message) {
        super(message);
    }
}
