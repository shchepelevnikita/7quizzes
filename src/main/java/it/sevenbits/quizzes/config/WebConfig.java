package it.sevenbits.quizzes.config;

import it.sevenbits.quizzes.core.credentials.UserCredentialsResolver;
import it.sevenbits.quizzes.core.interceptors.JwtAuthInterceptor;
import it.sevenbits.quizzes.core.services.ITokenService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Web config which contains configurations for interceptors and argument resolvers
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final ITokenService jwtService;

    /**
     * Web Config constructor
     * @param jwtService - jwt token service
     */
    public WebConfig(final ITokenService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new JwtAuthInterceptor(jwtService));
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new UserCredentialsResolver());
    }
}
