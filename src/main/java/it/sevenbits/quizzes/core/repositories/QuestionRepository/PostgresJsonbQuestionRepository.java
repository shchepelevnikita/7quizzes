package it.sevenbits.quizzes.core.repositories.QuestionRepository;

import it.sevenbits.quizzes.core.models.Question;
import it.sevenbits.quizzes.core.models.QuestionAnswer;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.List;

/**
 * Postgres Question Repository that works with JSONB format
 */
public class PostgresJsonbQuestionRepository implements IQuestionRepository {
    private final JdbcOperations jdbcOperations;

    /**
     * Constructor of Postgres Question Repository that works with JSONB format
     * @param jdbcOperations instance
     */
    public PostgresJsonbQuestionRepository(final JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public Question getQuestion(final String questionId) {
        List<QuestionAnswer> answersList = jdbcOperations.query("SELECT id, questionid, answerData ->> 'text' FROM answers_jsonb "
                + "WHERE questionid = ?", (resultSet, i) -> new QuestionAnswer(resultSet.getString(3),
                resultSet.getString(1)), questionId);
        return jdbcOperations.queryForObject("SELECT id, questionData->>'correctAnswerId', questionData->>'text', " +
                        "questionData->'score' FROM questions_jsonb "
                        + "WHERE questions_jsonb.id = ?", (resultSet, i) -> new Question(resultSet.getString(2),
                        resultSet.getString(3), answersList, resultSet.getInt(4), resultSet.getString(1)),
                questionId);
    }

    @Override
    public String getRandomQuestionId(final List<String> randomQuestionIdList) {
        String tempQuestionId = jdbcOperations.queryForObject("SELECT id FROM questions_jsonb ORDER BY random() LIMIT 1",
                (resultSet, i) -> resultSet.getString("id"));
        while (randomQuestionIdList.contains(tempQuestionId)) {
            tempQuestionId = jdbcOperations.queryForObject("SELECT id FROM questions_jsonb ORDER BY random() LIMIT 1",
                    (resultSet, i) -> resultSet.getString("id"));
        }
        return tempQuestionId;
    }
}
