package it.sevenbits.quizzes.core.repositories.GameRepository;

import it.sevenbits.quizzes.core.models.Game;
import it.sevenbits.quizzes.web.models.GameStatus;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.List;
import java.util.Random;

/**
 * version of game repo which works with postgres db
 */
public class PostgresGameRepository implements IGameRepository {
    private final JdbcOperations jdbcOperations;
    private final Random rand;

    /**
     * constructor of postgres game repo
     * @param jdbcOperations - jdbcoperations instance to initialize
     */
    public PostgresGameRepository(final JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
        this.rand = new Random();
    }

    /**
     * method to add the game to the room
     * @param roomId - id of the room
     * @param game - the game itself
     */
    @Override
    public void addGame(final String roomId, final Game game) {
        jdbcOperations.update("UPDATE rooms SET hasstarted = ?, status = ?, questionid = ?, questionnumber = ?, "
                        + "questionscount = ? WHERE id = ?",
                game.isGameStarted(), game.getGameStatus().getStatus(), game.getGameStatus().getQuestionId(),
                game.getGameStatus().getQuestionNumber(), game.getGameStatus().getQuestionsCount(), roomId);
    }

    /**
     * setter for isGameStarted
     * @param roomId - id of the room
     * @param value - value to be set
     */
    @Override
    public void setIsGameStarted(final String roomId, final boolean value) {
        jdbcOperations.update("UPDATE rooms SET hasstarted = ? WHERE id = ?", value, roomId);
    }

    /**
     * Getter for map isGameStarted which tells which game has started and which not
     * @param roomId - room id
     * @return - map string -> boolean isGameStarted
     */
    @Override
    public boolean getIsGameStarted(final String roomId) {
        return Boolean.TRUE.equals(jdbcOperations.queryForObject("SELECT hasstarted FROM rooms WHERE id = ?",
                Boolean.class, roomId));
    }

    /**
     * setter for total score
     * @param roomId - id of the room
     * @param playerId - id of the player
     * @param value - total score to be updated
     */
    @Override
    public void setTotalScore(final String roomId, final String playerId, final int value) {
        jdbcOperations.update("INSERT INTO players_stats_per_game (totalscore, playerid, roomid) VALUES (?, ?, ?)"
                + " ON CONFLICT (playerid, roomid) DO UPDATE SET totalscore = ? "
                + "WHERE players_stats_per_game.playerid = ? AND players_stats_per_game.roomid = ?",
                value, playerId, roomId, value, playerId, roomId);
    }

    /**
     * getter for total score
     * @param roomId - id of the room
     * @param playerId - id of the player
     * @return - total score
     */
    @Override
    public int getTotalScore(final String roomId, final String playerId) {
        return jdbcOperations.queryForObject("SELECT COALESCE(totalscore, 0) "
                + "FROM players_stats_per_game WHERE roomid = ? AND playerid = ?",
                Integer.class, roomId, playerId);
    }

    /**
     * There should be implementation of readiness setter for Postgres Repository but as I am only using Postgres JSONB
     * repository I don't need such implementation thus I have no desire to implement it
     * @param roomId - id of the room
     * @param playerId - id of the player
     * @param value - isReady value to be updated
     */
    @Override
    public void setIsReady(final String roomId, final String playerId, final boolean value) {}

    /**
     * There should be implementation of readiness getter for Postgres Repository but as I am only using Postgres JSONB
     * repository I don't need such implementation thus I have no desire to implement it
     * @param roomId - id of the room
     * @param playerId - id of the player
     */
    @Override
    public boolean getIsReady(final String roomId, final String playerId) {
        return false;
    }

    /**
     * Method to get current status of the game in certain room
     * @param roomId - id of the room
     * @return - current status of the game in certain room
     */
    @Override
    public GameStatus getGameStatus(final String roomId) {
        return jdbcOperations.queryForObject("SELECT status, questionid, questionnumber, questionscount FROM "
                + "rooms WHERE id = ?", (resultSet, i) -> new GameStatus(resultSet.getString("status"),
                resultSet.getString("questionid"), resultSet.getInt("questionnumber"),
                resultSet.getInt("questionscount")), roomId);
    }

    /**
     * Method to set certain status for game in certain room
     * @param roomId - id of the room
     * @param status - status of the game
     */
    @Override
    public void setGameStatus(final String roomId, final String status) {
        jdbcOperations.update("INSERT INTO rooms (id, status) VALUES (?, ?) ON CONFLICT (id) DO UPDATE SET "
                + "status = ?", roomId, status, status);
    }

    /**
     * Method to set current game question (which is part of the game status)
     * @param roomId - id of the room
     * @param questionId - id of the current question
     */
    @Override
    public void setGameQuestion(final String roomId, final String questionId) {
        jdbcOperations.update("INSERT INTO rooms (id, questionid) VALUES (?, ?) ON CONFLICT (id) DO UPDATE "
                + "SET questionid = ?", roomId, questionId, questionId);
    }

    /**
     * Method to set total number of questions for game (which is part of the game status)
     * @param roomId - id of the room
     * @param questionNumber - number of questions
     */
    @Override
    public void setGameQuestionNumber(final String roomId, final int questionNumber) {
        jdbcOperations.update("INSERT INTO rooms (id, questionnumber) VALUES (?, ?) ON CONFLICT (id) DO UPDATE "
                + "SET questionnumber = ?", roomId, questionNumber, questionNumber);
    }

    /**
     * Method to set current number of questions given to players (which is part of the game status)
     * @param roomId - id of the room
     * @param questionsCount - number of questions given to players
     */
    @Override
    public void setGameQuestionsCount(final String roomId, final int questionsCount) {
        jdbcOperations.update("INSERT INTO rooms (id, questionscount) VALUES (?, ?) ON CONFLICT (id) DO UPDATE "
                + "SET questionscount = ?", roomId, questionsCount, questionsCount);
    }

    /**
     * Getter for total number of questions for game
     * @param roomId - room id
     * @return - number of questions for game
     */
    @Override
    public int getAmountOfQuestions(final String roomId) {
        return jdbcOperations.queryForObject("SELECT questionnumber FROM rooms WHERE id = ?", Integer.class,
                roomId);
    }

    /**
     * Getter for number of questions already given to players
     * @param roomId - id of the room
     * @return - number of questions given to players already
     */
    @Override
    public int getGameQuestionsCount(final String roomId) {
        return jdbcOperations.queryForObject("SELECT questionscount FROM rooms WHERE id = ?", Integer.class,
                roomId);
    }

    /**
     * Getter of isQuestionAnswered value
     * @param roomId - room id
     * @param questionId - question id
     * @return map (roomId -> (questionId -> boolean value))
     */
    @Override
    public boolean getIsQuestionAnswered(final String roomId, final String questionId) {
        return Boolean.TRUE.equals(jdbcOperations.queryForObject("SELECT isanswered FROM rooms_questions WHERE "
                + "roomid = ? AND questionid = ?", Boolean.class, roomId, questionId));
    }

    /**
     * Method for setting question score
     * @param roomId - id of the room
     * @param playerId - id of the player
     * @param questionId - id of the question
     * @param value - value of score to set
     */
    @Override
    public void setQuestionScore(final String roomId, final String playerId, final String questionId, final int value) {
        jdbcOperations.update("INSERT INTO players_stats_per_question (roomid, playerid, questionid, "
                + "questionscore) VALUES (?, ?, ?, ?) ON CONFLICT (roomid, playerid, questionid) DO UPDATE SET "
                + "questionscore = ?", roomId, playerId, questionId, value, value);
    }

    /**
     * Method for getting question score
     * @param roomId - id of the room
     * @param playerId - id of the player
     * @param questionId - id of the question
     * @return - question score for certain room, player and question
     */
    @Override
    public int getQuestionScore(final String roomId, final String playerId, final String questionId) {
        return jdbcOperations.queryForObject("SELECT questionscore FROM players_stats_per_question WHERE "
                + "roomid = ? AND playerid = ? AND questionid = ?", Integer.class, roomId, playerId, questionId);
    }

    /**
     * Method to get random question id of those which belong to certain game
     * @param roomId - id of the room
     * @return - random question id
     */
    @Override
    public String getRandomQuestionId(final String roomId) {
        List<String> questionIdList = getQuestionIdsPerGame(roomId);
        String tempQuestionId = questionIdList.get(rand.nextInt(questionIdList.size()));
        while (getIsQuestionAnswered(roomId, tempQuestionId)) {
            tempQuestionId = questionIdList.get(rand.nextInt(questionIdList.size()));
        }
        return tempQuestionId;
    }

    /**
     * Method to set whether the question has been answered
     * @param roomId - id of the room
     * @param questionId - id of the question
     */
    @Override
    public void setIsQuestionAnswered(final String roomId, final String questionId) {
        jdbcOperations.update("INSERT INTO rooms_questions (roomid, questionid, isanswered) VALUES (?, ?, ?) "
                + "ON CONFLICT (roomid, questionid) DO UPDATE SET isanswered = ?", roomId, questionId, true, true);
    }

    /**
     * Method to get current game question
     * @param roomId - id of the room
     * @return - current game question
     */
    @Override
    public String getGameQuestion(final String roomId) {
        return jdbcOperations.queryForObject("SELECT questionid FROM rooms WHERE id = ?", String.class, roomId);
    }

    /**
     * Method to get hasPlayerAnswered value
     * @param roomId - room id
     * @param questionId - id of the question
     * @param playerId - id of the player
     * @return - hasPlayerAnswered map
     */
    @Override
    public boolean getHasPlayerAnswered(final String roomId, final String playerId, final String questionId) {
        return Boolean.TRUE.equals(jdbcOperations.queryForObject("SELECT hasanswered FROM "
                + "players_stats_per_question WHERE roomid = ? AND playerid = ? AND questionid = ?",
                Boolean.class, roomId, playerId, questionId));
    }

    /**
     * Method to set a value in hasPlayerAnswered table
     * @param roomId - id of the room
     * @param playerId - id of the player
     * @param questionId - id of the question
     * @param value - value of whether the player has answered given question or not
     */
    @Override
    public void setHasPlayerAnswered(final String roomId, final String playerId, final String questionId, final boolean
            value) {
        jdbcOperations.update("INSERT INTO players_stats_per_question (roomid, playerid, questionid, "
                + "hasanswered) VALUES (?, ?, ?, ?) ON CONFLICT (roomid, playerid, questionid) DO UPDATE SET "
                + "hasanswered = ?", roomId, playerId, questionId, value, value);
    }

    /**
     * set question ids for certain game
     * @param roomId - id of the room
     * @param questionIdsPerGame - question ids to initialize in the game
     */
    @Override
    public void setQuestionIdsPerGame(final String roomId, final List<String> questionIdsPerGame) {
        for (String questionId : questionIdsPerGame) {
            jdbcOperations.update("INSERT INTO rooms_questions (roomid, questionid, isanswered) VALUES (?, ?, ?) ON " +
                    "CONFLICT (roomid, questionid) DO UPDATE SET isanswered = ?", roomId, questionId, false, false);
        }
    }

    /**
     * get question ids from certain game
     * @param roomId - id of the room
     * @return - question ids
     */
    @Override
    public List<String> getQuestionIdsPerGame(final String roomId) {
        return jdbcOperations.query("SELECT rooms_questions.questionid FROM rooms_questions "
                + "WHERE rooms_questions.roomid = ?", (resultSet, i) -> resultSet.getString("questionid"), roomId);
    }

    /**
     * checks whether the game exists or not
     * @param roomId - id of the room
     * @return boolean value which tells us whether the game exist or not
     */
    @Override
    public boolean isGameExisting(final String roomId) {
        return getGameStatus(roomId).getStatus() != null;
    }

    @Override
    public String getRules() {
        return jdbcOperations.queryForObject("SELECT rules FROM rules", String.class);
    }
}
