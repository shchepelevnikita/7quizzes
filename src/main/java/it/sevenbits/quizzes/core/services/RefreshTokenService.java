package it.sevenbits.quizzes.core.services;

import it.sevenbits.quizzes.core.exceptions.TokenRefreshException;
import it.sevenbits.quizzes.core.models.User;
import it.sevenbits.quizzes.core.repositories.RefreshTokenRepository;
import it.sevenbits.quizzes.core.repositories.UserRepository.IUserRepository;
import it.sevenbits.quizzes.core.models.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Service that contains logic of working with refresh tokens
 */
@Service
public class RefreshTokenService {
    @Value("${refreshToken.ExpirationMs}")
    private Long refreshTokenDurationMs;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private IUserRepository userRepository;

    /**
     * Find RefreshToken by token value. Optional is used because of the map method.
     * @param token - token value
     * @return - refresh token wrapped in Optional
     */
    public Optional<RefreshToken> findByToken(final String token) {
        return Optional.ofNullable(refreshTokenRepository.findRefreshTokenByToken(token));
    }

    /**
     * Method to create refresh token
     * @param userId - user id
     * @return refresh token
     */
    public RefreshToken createRefreshToken(final String userId) {
        RefreshToken refreshToken = new RefreshToken(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                userId,
                Instant.now().plusMillis(refreshTokenDurationMs));

        refreshTokenRepository.saveRefreshToken(refreshToken);
        return refreshToken;
    }

    /**
     * Method to check if the refresh token is expired
     * @param refreshToken - refresh token
     * @return user - owner of the token
     */
    public User verifyExpiration(final RefreshToken refreshToken) {
        if (refreshToken.getExpireDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.deleteRefreshTokenByToken(refreshToken.getToken());
            throw new TokenRefreshException(refreshToken.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return userRepository.getUserById(refreshToken.getUserId());
    }
}
