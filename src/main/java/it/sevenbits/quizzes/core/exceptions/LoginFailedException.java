package it.sevenbits.quizzes.core.exceptions;

/**
 * Exception that occurs in case of failed login
 */
public class LoginFailedException extends RuntimeException {
    /**
     * Exception constructor
     * @param message - message of exception
     */
    public LoginFailedException(final String message) {
        super(message);
    }
}
