package it.sevenbits.quizzes.core.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import it.sevenbits.quizzes.config.JwtSettings;
import it.sevenbits.quizzes.core.credentials.IUserCredentials;
import it.sevenbits.quizzes.core.credentials.UserCredentials;
import it.sevenbits.quizzes.core.models.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Service for JWT authorization
 */
@Service
public class JwtService implements ITokenService {
    private static final long TIME_MULTIPLIER = 60L;
    private final JwtSettings settings;
    private static final String ROLES = "roles";

    /**
     * JWT Service constructor
     * @param settings - settings of jwt auth
     */
    public JwtService(final JwtSettings settings) {
        this.settings = settings;
    }

    /**
     * Method for creating token based on user credentials
     * @param user - user instance
     * @return - token
     */
    @Override
    public String createToken(final User user) {
        Instant now = Instant.now();

        Claims claims = Jwts.claims()
                .setIssuer(settings.getTokenIssuer())
                .setIssuedAt(Date.from(now))
                .setSubject(user.getUserId())
                .setExpiration(Date.from(now.plusSeconds(settings.getTokenExpiredIn() * TIME_MULTIPLIER)));
        claims.put(ROLES, user.getRoles());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                .compact();
    }

    /**
     * Method for parsing token
     * @param token - jwt token
     * @return - user credentials
     */
    @Override
    @SuppressWarnings("unchecked")
    public IUserCredentials parseToken(final String token) {
        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(settings.getTokenSigningKey()).parseClaimsJws(token);

        String subject = claims.getBody().getSubject();
        List<String> roles = claims.getBody().get(ROLES, List.class);

        return new UserCredentials(subject, Collections.unmodifiableList(roles));
    }
}
