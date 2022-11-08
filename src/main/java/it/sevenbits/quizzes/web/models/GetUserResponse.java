package it.sevenbits.quizzes.web.models;

import java.util.List;

/**
 * Model containing user data for response
 */
public class GetUserResponse {
    private final String userId;
    private final String email;
    private final List<String> roles;

    /**
     * Model's constructor
     * @param userId - user id
     * @param email - user email
     * @param roles - user roles
     */
    public GetUserResponse(final String userId, final String email, final List<String> roles) {
        this.userId = userId;
        this.email = email;
        this.roles = roles;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }
}
