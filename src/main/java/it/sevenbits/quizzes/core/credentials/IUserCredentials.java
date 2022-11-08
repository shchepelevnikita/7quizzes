package it.sevenbits.quizzes.core.credentials;

import java.util.List;

/**
 * Interface for user credentials model
 */
public interface IUserCredentials {
    /**
     * Method for getting user id
     * @return - user id
     */
    String getUserId();

    /**
     * Method for getting user's roles
     * @return - user roles
     */
    List<String> getRoles();
}
