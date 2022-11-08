package it.sevenbits.quizzes.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Settings for jwt authorization
 */
@Configuration
public class JwtSettings {
    private final String tokenIssuer;
    private final String tokenSigningKey;
    private final int aTokenDuration;

    /**
     * jwt settings constructor
     * @param tokenIssuer - who issued the token
     * @param tokenSigningKey - key used to sign jwt token
     * @param aTokenDuration - duration of jwt token after which it expires
     */
    public JwtSettings(@Value("${jwt.issuer}") final String tokenIssuer,
                       @Value("${jwt.signingKey}") final String tokenSigningKey,
                       @Value("${jwt.aTokenDuration}") final int aTokenDuration) {
        this.tokenIssuer = tokenIssuer;
        this.tokenSigningKey = tokenSigningKey;
        this.aTokenDuration = aTokenDuration;
    }

    public String getTokenIssuer() {
        return tokenIssuer;
    }

    public String getTokenSigningKey() {
        return tokenSigningKey;
    }

    public int getTokenExpiredIn() {
        return aTokenDuration;
    }
}
