package it.sevenbits.quizzes.config;

import it.sevenbits.quizzes.core.encoders.BCryptPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Config for password encoders
 */
@Configuration
public class PasswordEncoderConfig {
    private static final int LOG_ROUNDS = 10;

    /**
     * Bean which returns instance of bcrypt password encoder
     * @return - instance of bcrypt password encoder
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(LOG_ROUNDS);
    }
}
