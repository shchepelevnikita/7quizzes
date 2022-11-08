package it.sevenbits.quizzes.core.encoders;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Class for encrypting passwords
 */
public class BCryptPasswordEncoder {
    private final int logRounds;

    /**
     *  BCrypt encoder constructor
     * @param logRounds - number of hashing rounds
     */
    public BCryptPasswordEncoder(final int logRounds) {
        this.logRounds = logRounds;
    }

    /**
     * Method for comparing plain password and hashed password
     * @param plainPassword - plain password
     * @param hashedPassword - hashed password
     * @return - boolean value which tells whether the passwords match
     */
    public boolean matches(final String plainPassword, final String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    /**
     * Method for hashing password with salt
     * @param plainPassword - plain password
     * @return - hashed password
     */
    public String hash(final String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(logRounds));
    }
}
