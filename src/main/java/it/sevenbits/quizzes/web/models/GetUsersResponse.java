package it.sevenbits.quizzes.web.models;

import it.sevenbits.quizzes.core.models.User;

import java.util.List;

/**
 * Response model for getting all users
 */
public class GetUsersResponse {
    private final List<User> users;

    /**
     * Model constructor
     * @param users - list with all users
     */
    public GetUsersResponse(final List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }
}
