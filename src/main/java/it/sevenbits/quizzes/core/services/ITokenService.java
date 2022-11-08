package it.sevenbits.quizzes.core.services;

import it.sevenbits.quizzes.core.credentials.IUserCredentials;
import it.sevenbits.quizzes.core.models.User;

/**
 * Interface for token service
 */
public interface ITokenService {
    /**
     * Method for creating token based on user credentials
     * @param user - user instance
     * @return - token
     */
    String createToken(User user);
    /**
     * Method for parsing token
     * @param token - jwt token
     * @return - user credentials
     */
    IUserCredentials parseToken(String token);
}
