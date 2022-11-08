package it.sevenbits.quizzes.core.repositories.UserRepository;

import com.google.gson.Gson;
import it.sevenbits.quizzes.core.models.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.List;
import java.util.UUID;

/**
 * Postgres User Repository that works with JSONB format
 */
public class PostgresJsonbUserRepository implements IUserRepository {
    private final JdbcOperations jdbcOperations;
    private final Gson gson;

    /**
     * Constructor of Postgres User Repository that works with JSONB format
     * @param jdbcOperations instance
     */
    public PostgresJsonbUserRepository(final JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
        this.gson = new Gson();
    }

    @Override
    public void createUser(final String email, final String password, final List<String> roles) {
        User user = new User(UUID.randomUUID().toString(), email, password, roles);
        gson.toJson(user);
        jdbcOperations.update("INSERT INTO users_jsonb (id, userData) VALUES (?, ?::jsonb)",
                user.getUserId(), gson.toJson(user));
        jdbcOperations.update("INSERT INTO user_roles_jsonb (userid, \"role\") VALUES (?, ?)", user.getUserId(),
                user.getRoles().get(0));
    }

    @Override
    public User getUserByEmail(final String email) {
        try {
            return jdbcOperations.queryForObject("SELECT id, userData, userData->>'email', userData->>'password' " +
                    "FROM users_jsonb WHERE userData->>'email' = ?", (resultSet, i) ->
                    new User(resultSet.getString(1), resultSet.getString(3), resultSet.getString(4), jdbcOperations.query(
                            "SELECT userid, \"role\" FROM user_roles_jsonb WHERE userid = ?",
                            (userResultSet, j) -> userResultSet.getString(2), resultSet.getString(1))), email);
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    public User getUserById(final String userId) {
        return jdbcOperations.queryForObject("SELECT id, userData->>'email', userData->>'password' FROM users_jsonb WHERE id = ?",
                (resultSet, i) ->
                new User(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), jdbcOperations.query(
                        "SELECT userid, \"role\" FROM user_roles_jsonb WHERE userid = ?",
                        (userResultSet, j) -> userResultSet.getString(2), userId)), userId);
    }

    @Override
    public List<User> getUsers() {
        return jdbcOperations.query("SELECT id, userData->>'email', userData->>'password' FROM users_jsonb", (resultSet, i) ->
                new User(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), jdbcOperations.query(
                        "SELECT userid, \"role\" FROM user_roles_jsonb WHERE userid = ?",
                        (userResultSet, j) -> userResultSet.getString(2), resultSet.getString(1))));
    }
}
