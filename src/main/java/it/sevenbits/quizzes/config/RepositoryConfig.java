package it.sevenbits.quizzes.config;

import it.sevenbits.quizzes.core.repositories.GameRepository.IGameRepository;
import it.sevenbits.quizzes.core.repositories.GameRepository.PostgresJsonbGameRepository;
import it.sevenbits.quizzes.core.repositories.QuestionRepository.IQuestionRepository;
import it.sevenbits.quizzes.core.repositories.QuestionRepository.PostgresJsonbQuestionRepository;
import it.sevenbits.quizzes.core.repositories.RefreshTokenRepository;
import it.sevenbits.quizzes.core.repositories.RoomRepository.IRoomRepository;
import it.sevenbits.quizzes.core.repositories.RoomRepository.PostgresJsonbRoomRepository;
import it.sevenbits.quizzes.core.repositories.UserRepository.IUserRepository;
import it.sevenbits.quizzes.core.repositories.UserRepository.PostgresJsonbUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;

/**
 * Config to choose repos to work with
 */
@Configuration
public class RepositoryConfig {

    /**
     * Returns game repository
     * @param jdbcOperations jdbcoperations instance
     * @return returns game repository
     */
    @Bean
    public IGameRepository gameRepository(final JdbcOperations jdbcOperations) {
        // return new PostgresGameRepository(jdbcOperations);
        // return new GameRepository();
        return new PostgresJsonbGameRepository(jdbcOperations);
    }

    /**
     * Returns room repository
     * @param jdbcOperations jdbcoperations instance
     * @return returns room repository
     */
    @Bean
    public IRoomRepository roomRepository(final JdbcOperations jdbcOperations) {
        // return new PostgresRoomRepository(jdbcOperations);
        // return new RoomRepository();
        return new PostgresJsonbRoomRepository(jdbcOperations);
    }

    /**
     * Returns question repository
     * @param jdbcOperations jdbcoperations instance
     * @return returns question repository
     */
    @Bean
    public IQuestionRepository questionRepository(final JdbcOperations jdbcOperations) {
        // return new PostgresQuestionRepository(jdbcOperations);
        // return new QuestionRepository();
        return new PostgresJsonbQuestionRepository(jdbcOperations);
    }

    /**
     * Returns user repository
     * @param jdbcOperations jdbcoperations instance
     * @return returns user repository
     */
    @Bean
    public IUserRepository userRepository(final JdbcOperations jdbcOperations) {
        // return new PostgresUserRepository(jdbcOperations);
        return new PostgresJsonbUserRepository(jdbcOperations);
    }

    /**
     * Return refresh token repository
     * @param jdbcOperations jdbcoperations instance
     * @return returns refresh token repository
     */
    @Bean
    RefreshTokenRepository refreshTokenRepository(final JdbcOperations jdbcOperations) {
        return new RefreshTokenRepository(jdbcOperations);
    }
}
