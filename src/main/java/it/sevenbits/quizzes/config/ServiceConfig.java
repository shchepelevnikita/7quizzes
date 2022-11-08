package it.sevenbits.quizzes.config;

import it.sevenbits.quizzes.core.services.ITokenService;
import it.sevenbits.quizzes.core.services.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Service configuration
 */
@Configuration
public class ServiceConfig {
    /**
     * Bean which provides jwtTokenService
     * @param settings - jwt settings
     * @return - instance of jwt token service
     */
    @Bean
    public ITokenService tokenService(final JwtSettings settings) {
        return new JwtService(settings);
    }
}
