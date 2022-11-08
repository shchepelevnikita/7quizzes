package it.sevenbits.quizzes.core.exceptions;

/**
 * Token refresh exception deals with expired tokens
 */
public class TokenRefreshException extends RuntimeException {
    /**
     * Exception constructor
     * @param token - refresh token
     * @param message - message
     */
    public TokenRefreshException(final String token, final String message) {
        super(String.format("Failed for [%s]: %s", token, message));
    }
}
