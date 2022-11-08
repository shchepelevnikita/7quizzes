package it.sevenbits.quizzes.core.repositories.QuestionRepository;

import it.sevenbits.quizzes.core.models.Question;
import it.sevenbits.quizzes.core.models.QuestionAnswer;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.List;

/**
 * Variant of question repository that uses postgres
 */
public class PostgresQuestionRepository implements IQuestionRepository {
    private final JdbcOperations jdbcOperations;

    /**
     * postgres question repo constructor that initializes jdbcoperations instance
     * @param jdbcOperations - jdbcoperations instance to initialize
     */
    public PostgresQuestionRepository(final JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    /**
     * get question from db by questionid
     * @param questionId - questionId
     * @return question
     */
    @Override
    public Question getQuestion(final String questionId) {
        List<QuestionAnswer> answersList = jdbcOperations.query("SELECT id, questionid, \"text\" FROM answers "
                + "WHERE questionid = ?", (resultSet, i) -> new QuestionAnswer(resultSet.getString("text"),
                resultSet.getString("id")), questionId);
        return jdbcOperations.queryForObject("SELECT id, correctanswerid, \"text\", score FROM questions "
                + "WHERE questions.id = ?", (resultSet, i) -> new Question(resultSet.getString("correctanswerid"),
                resultSet.getString("text"), answersList, resultSet.getInt("score"), resultSet.getString("id")),
                questionId);
    }

    /**
     * get randpm question from db
     * @param randomQuestionIdList - list of random question ids
     * @return random question
     */
    @Override
    public String getRandomQuestionId(final List<String> randomQuestionIdList) {
        String tempQuestionId = jdbcOperations.queryForObject("SELECT id FROM questions ORDER BY random() LIMIT 1",
                (resultSet, i) -> resultSet.getString("id"));
        while (randomQuestionIdList.contains(tempQuestionId)) {
            tempQuestionId = jdbcOperations.queryForObject("SELECT id FROM questions ORDER BY random() LIMIT 1",
                    (resultSet, i) -> resultSet.getString("id"));
        }
        return tempQuestionId;
    }
}
