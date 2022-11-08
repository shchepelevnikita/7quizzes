package it.sevenbits.quizzes.core.repositories;

import it.sevenbits.quizzes.core.models.User;
import it.sevenbits.quizzes.core.models.RefreshToken;
import org.springframework.jdbc.core.JdbcOperations;

import java.time.Instant;

/**
 * Repository that has operations to work with refresh token, which is store in the database
 */
public class RefreshTokenRepository {
    private final JdbcOperations jdbcOperations;

    /**
     * Repository constructor
     * @param jdbcOperations - jdbcoperations instance
     */
    public RefreshTokenRepository(final JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    /**
     * Find refresh token in database by User
     * @param user - user
     * @return - refresh token
     */
    public RefreshToken findRefreshTokenByUser(final User user) {
        return jdbcOperations.queryForObject("SELECT id, token, userId, expireDate FROM refresh_token WHERE userId = ?",
                (resultSet, i) -> new RefreshToken(resultSet.getString(1), resultSet.getString(2),
                        resultSet.getString(3), Instant.parse(resultSet.getString(4))),
                user.getUserId());
    }

    /**
     * Find refresh token in database by token value
     * @param token - token value
     * @return - refresh token
     */
    public RefreshToken findRefreshTokenByToken(final String token) {
        return jdbcOperations.queryForObject("SELECT id, token, userId, expireDate FROM refresh_token WHERE token = ?",
                (resultSet, i) -> new RefreshToken(resultSet.getString(1), resultSet.getString(2),
                        resultSet.getString(3), Instant.parse(resultSet.getString(4))),
                token);
    }

    /**
     * Save refresh token into the database
     * @param refreshToken - refresh token
     */
    public void saveRefreshToken(final RefreshToken refreshToken) {
        jdbcOperations.update("INSERT INTO refresh_token (id, token, userId, expireDate) VALUES (?, ?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET token = ?, userId = ?, expireDate = ?", refreshToken.getId(),
                refreshToken.getToken(), refreshToken.getUserId(), refreshToken.getExpireDate().toString(),
                refreshToken.getToken()
        , refreshToken.getUserId(), refreshToken.getExpireDate().toString());
    }

    /**
     * Delete refresh token from the database by userId
     * @param userId - userId
     */
    public void deleteRefreshTokenByUserId(final String userId) {
        jdbcOperations.update("DELETE FROM refresh_token WHERE refresh_token.userId = ?", userId);
    }

    /**
     * Delete refresh token from the database by token value
     * @param token - token value
     */
    public void deleteRefreshTokenByToken(final String token) {
        jdbcOperations.update("DELETE FROM refresh_token WHERE refresh_token.token = ?", token);
    }
}
