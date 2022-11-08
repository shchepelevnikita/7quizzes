package it.sevenbits.quizzes.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request model for sign up. Contains email and password for registration purposes.
 */
public class SignUpRequest {
    private final String email;
    private final String password;

    /**
     * Constructor for initializing email and password.
     * @param email - user email
     * @param password - user password
     */
    public SignUpRequest(@JsonProperty("email")final String email, @JsonProperty("password")final String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
