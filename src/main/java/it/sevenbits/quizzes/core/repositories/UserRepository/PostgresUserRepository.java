package it.sevenbits.quizzes.core.repositories.UserRepository;

import it.sevenbits.quizzes.core.models.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.List;
import java.util.UUID;

/**
 * User repository that works with postgres db
 */
public class PostgresUserRepository implements IUserRepository {
    private final JdbcOperations jdbcOperations;

    /**
     * postgres user repo constructor
     * @param jdbcOperations - jdbcoperations instance
     */
    public PostgresUserRepository(final JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public void createUser(final String email, final String password, final List<String> roles) {
        User user = new User(UUID.randomUUID().toString(), email, password, roles);
        jdbcOperations.update("INSERT INTO users (id, email, \"password\", enabled) VALUES (?, ?, ?, ?)",
                user.getUserId(), user.getEmail(), user.getPassword(), true);
        jdbcOperations.update("INSERT INTO user_roles (userid, \"role\") VALUES (?, ?)", user.getUserId(), user.getRoles().get(0));
    }

    @Override
    public User getUserByEmail(final String email) {
        try {
            return jdbcOperations.queryForObject("SELECT id, email, \"password\" FROM users WHERE email = ?", (resultSet, i) ->
                    new User(resultSet.getString("id"), resultSet.getString("email"), resultSet.getString("password"), jdbcOperations.query(
                            "SELECT userid, \"role\" FROM user_roles WHERE userid = ?",
                            (userResultSet, j) -> userResultSet.getString("role"), resultSet.getString("id"))), email);
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    public User getUserById(final String userId) {
        return jdbcOperations.queryForObject("SELECT id, email, \"password\" FROM users WHERE id = ?", (resultSet, i) ->
                new User(resultSet.getString("id"), resultSet.getString("email"), resultSet.getString("password"), jdbcOperations.query(
                        "SELECT userid, \"role\" FROM user_roles WHERE userid = ?",
                        (userResultSet, j) -> userResultSet.getString("role"), userId)), userId);
    }

    @Override
    public List<User> getUsers() {
        return jdbcOperations.query("SELECT id, email, \"password\" FROM users", (resultSet, i) ->
                new User(resultSet.getString("id"), resultSet.getString("email"), resultSet.getString("password"), jdbcOperations.query(
                        "SELECT userid, \"role\" FROM user_roles WHERE userid = ?",
                        (userResultSet, j) -> userResultSet.getString("role"), resultSet.getString("id"))));
    }
}
