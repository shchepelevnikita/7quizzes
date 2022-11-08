package it.sevenbits.quizzes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Cors Configuration (allows frontend to make requests)
 */
@Configuration
public class CorsConfig {
    private static final int MAX_AGE = 3600;

    /**
     * The cors configurer which returns WebMvcConfigurer bean and allows frontend to make GET and POST requests
     * @return WebMvcConfigurer bean
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(final CorsRegistry registry) { // CorsRegistry
                registry.addMapping("/**") // CorsRegistration
                        .allowedOriginPatterns("*")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true).maxAge(MAX_AGE);
            }
        };
    }
}
