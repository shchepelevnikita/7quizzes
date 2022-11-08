package it.sevenbits.quizzes.core.credentials;

import java.util.List;

/**
 * Model for user credentials
 */
public class UserCredentials implements IUserCredentials {
    private final String userId;
    private final List<String> roles;

    /**
     * User credentials constructor
     * @param userId - user id
     * @param roles - user roles
     */
    public UserCredentials(final String userId, final List<String> roles) {
        this.userId = userId;
        this.roles = roles;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public List<String> getRoles() {
        return roles;
    }
}
