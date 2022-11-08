package it.sevenbits.quizzes.core.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * User model
 */
public class User {
    @JsonProperty("userId")
    private final String userId;
    @JsonProperty("email")
    private final String email;
    @JsonIgnore
    private final String password;
    @JsonProperty("roles")
    private final List<String> roles;

    /**
     * User model constructor
     * @param userId - user id
     * @param email - user email
     * @param password - user password
     * @param roles - user roles
     */
    public User(final String userId, final String email, final String password, final List<String> roles) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getRoles() {
        return roles;
    }
}
