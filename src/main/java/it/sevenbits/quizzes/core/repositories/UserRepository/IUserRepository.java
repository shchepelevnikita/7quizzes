package it.sevenbits.quizzes.core.repositories.UserRepository;

import it.sevenbits.quizzes.core.models.User;

import java.util.List;

/**
 * Interface for user repo
 */
public interface IUserRepository {
    /**
     * Method for creating user
     * @param email - user email
     * @param password - user password
     * @param roles - user roles
     */
    void createUser(String email, String password, List<String> roles);

    /**
     * Method for getting user's email
     * @param email - user email
     * @return - user
     */
    User getUserByEmail(String email);

    /**
     * Method for getting user by id
     * @param userId - user's id
     * @return - user
     */
    User getUserById(String userId);

    /**
     * Method for getting all users
     * @return - array of users
     */
    List<User> getUsers();
}
