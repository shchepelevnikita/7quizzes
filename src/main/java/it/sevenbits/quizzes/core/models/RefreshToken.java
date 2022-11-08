package it.sevenbits.quizzes.core.models;

import java.time.Instant;

/**
 * Refresh token model, which contains id of the token, its value, id of the owner and date of expire
 */
public class RefreshToken {
    private final String id;
    private final String token;
    private final String userId;
    private final Instant expireDate;

    /**
     * Refresh token constructor
     * @param id - token id
     * @param token - token value
     * @param userId - owner id
     * @param expireDate - date of expire
     */
    public RefreshToken(final String id, final String token, final String userId, final Instant expireDate) {
        this.id = id;
        this.token = token;
        this.userId = userId;
        this.expireDate = expireDate;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
    }

    public Instant getExpireDate() {
        return expireDate;
    }
}
