package it.sevenbits.quizzes.core.repositories.GameRepository;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.sevenbits.quizzes.core.models.Game;
import it.sevenbits.quizzes.web.models.GameStatus;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.List;
import java.util.Random;

/**
 * Postgres Game Repository that works with JSONB format
 */
public class PostgresJsonbGameRepository implements IGameRepository {
    private final JdbcOperations jdbcOperations;
    private final Random rand;
    private final Gson gson;

    /**
     * constructor of postgres game repo
     * @param jdbcOperations - jdbcoperations instance to initialize
     */
    public PostgresJsonbGameRepository(final JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
        this.rand = new Random();
        this.gson = new Gson();
    }

    /**
     * method to add the game to the room
     * @param roomId - id of the room
     * @param game - the game itself
     */
    @Override
    public void addGame(final String roomId, final Game game) {
        jdbcOperations.update("UPDATE rooms_jsonb SET roomData = roomData || ?::jsonb WHERE id = ?",
                gson.toJson(game), roomId);
    }

    /**
     * setter for isGameStarted
     * @param roomId - id of the room
     * @param value - value to be set
     */
    @Override
    public void setIsGameStarted(final String roomId, final boolean hasStarted) {
        jdbcOperations.update("UPDATE rooms_jsonb SET roomData = jsonb_set(roomData, '{ hasStarted }', ?::jsonb, " +
                "TRUE) WHERE id = ?", gson.toJson(hasStarted), roomId);
    }

    /**
     * Getter for map isGameStarted which tells which game has started and which not
     * @param roomId - room id
     * @return - map string -> boolean isGameStarted
     */
    @Override
    public boolean getIsGameStarted(final String roomId) {
        return Boolean.TRUE.equals(jdbcOperations.queryForObject("SELECT roomData->>'hasStarted' FROM rooms_jsonb " +
                        "WHERE id = ?",
                Boolean.class, roomId));
    }

    /**
     * setter for total score
     * @param roomId - id of the room
     * @param playerId - id of the player
     * @param value - total score to be updated
     */
    @Override
    public void setTotalScore(final String roomId, final String playerId, final int totalScore) {
        String playerStatsPerGame = jdbcOperations.queryForObject("SELECT playerStatsPerGame FROM " +
                        "players_stats_per_game_jsonb WHERE playerid = ? AND roomid = ?", String.class,
                playerId, roomId);

        JsonObject json = JsonParser.parseString(playerStatsPerGame).getAsJsonObject();
        json.addProperty("totalScore", totalScore);

        jdbcOperations.update("INSERT INTO players_stats_per_game_jsonb (playerStatsPerGame, playerid, roomid) " +
                        "VALUES (?::jsonb, ?, ?)"
                        + " ON CONFLICT (playerid, roomid) DO UPDATE SET playerstatspergame = ?::jsonb "
                        + "WHERE players_stats_per_game_jsonb.playerid = ? AND players_stats_per_game_jsonb.roomid = ?",
                gson.toJson(json), playerId, roomId, gson.toJson(json), playerId, roomId);
    }

    /**
     * getter for total score
     * @param roomId - id of the room
     * @param playerId - id of the player
     * @return - total score
     */
    @Override
    public int getTotalScore(final String roomId, final String playerId) {
        return jdbcOperations.queryForObject("SELECT playerStatsPerGame->>'totalScore' "
                        + "FROM players_stats_per_game_jsonb WHERE roomid = ? AND playerid = ?",
                Integer.class, roomId, playerId);
    }

    @Override
    public void setIsReady(final String roomId, final String playerId, final boolean value) {
        String playerStatsPerGame = jdbcOperations.queryForObject("SELECT playerStatsPerGame FROM " +
                        "players_stats_per_game_jsonb WHERE playerid = ? AND roomid = ?", String.class,
                playerId, roomId);

        JsonObject json = JsonParser.parseString(playerStatsPerGame).getAsJsonObject();
        json.addProperty("isReady", value);

        jdbcOperations.update("INSERT INTO players_stats_per_game_jsonb (playerStatsPerGame, playerid, roomid) " +
                        "VALUES (?::jsonb, ?, ?)"
                        + " ON CONFLICT (playerid, roomid) DO UPDATE SET playerstatspergame = ?::jsonb "
                        + "WHERE players_stats_per_game_jsonb.playerid = ? AND players_stats_per_game_jsonb.roomid = ?",
                gson.toJson(json), playerId, roomId, gson.toJson(json), playerId, roomId);
    }

    @Override
    public boolean getIsReady(final String roomId, final String playerId) {
        try {
                boolean isReady = jdbcOperations.queryForObject("SELECT playerStatsPerGame->>'isReady' "
                                + "FROM players_stats_per_game_jsonb WHERE roomid = ? AND playerid = ?",
                        Boolean.class, roomId, playerId);
                return isReady;
        } catch (NullPointerException ex) {
                return false;
        }
    }

    /**
     * Method to get current status of the game in certain room
     * @param roomId - id of the room
     * @return - current status of the game in certain room
     */
    @Override
    public GameStatus getGameStatus(final String roomId) {
        String roomData = jdbcOperations.queryForObject("SELECT roomData FROM rooms_jsonb WHERE rooms_jsonb.id = ?",
                String.class,
                roomId);

        return jdbcOperations.queryForObject("SELECT roomData->'gameStatus'->>'status', " +
                "roomData->'gameStatus'->>'questionId', roomData->'gameStatus'->>'questionNumber', " +
                "roomData->'gameStatus'->>'questionsCount' FROM "
                + "rooms_jsonb WHERE id = ?", (resultSet, i) -> new GameStatus(resultSet.getString(1),
                resultSet.getString(2), resultSet.getInt(3),
                resultSet.getInt(4)), roomId);
    }

    /**
     * Method to set certain status for game in certain room
     * @param roomId - id of the room
     * @param status - status of the game
     */
    @Override
    public void setGameStatus(final String roomId, final String status) {
        String roomData = jdbcOperations.queryForObject("SELECT roomData FROM rooms_jsonb WHERE rooms_jsonb.id = ?",
                String.class,
                roomId);
        String gameStatus = jdbcOperations.queryForObject("SELECT roomData->'gameStatus' FROM rooms_jsonb WHERE " +
                        "rooms_jsonb.id = ?", String.class,
                roomId);

        JsonObject json = JsonParser.parseString(gameStatus).getAsJsonObject();
        json.addProperty("status", status);

        jdbcOperations.update("INSERT INTO rooms_jsonb (id, roomData) VALUES (?, " +
                "jsonb_set(?::jsonb, '{ gameStatus }', ?::jsonb, TRUE)) ON CONFLICT (id) DO UPDATE SET "
                + "roomData = jsonb_set(rooms_jsonb.roomData, '{ gameStatus }', ?::jsonb, TRUE)", roomId, roomData,
                gson.toJson(json), gson.toJson(json));
    }

    /**
     * Method to set current game question (which is part of the game status)
     * @param roomId - id of the room
     * @param questionId - id of the current question
     */
    @Override
    public void setGameQuestion(final String roomId, final String questionId) {
        String roomData = jdbcOperations.queryForObject("SELECT roomData FROM rooms_jsonb WHERE rooms_jsonb.id = ?",
                String.class,
                roomId);
        String gameStatus = jdbcOperations.queryForObject("SELECT roomData->'gameStatus' FROM rooms_jsonb WHERE " +
                        "rooms_jsonb.id = ?", String.class,
                roomId);

        JsonObject json = JsonParser.parseString(gameStatus).getAsJsonObject();
        json.addProperty("questionId", questionId);

        jdbcOperations.update("INSERT INTO rooms_jsonb (id, roomData) VALUES (?, " +
                "jsonb_set(?::jsonb, '{ gameStatus }', ?::jsonb, TRUE)) ON CONFLICT (id) DO UPDATE "
                + "SET roomData = jsonb_set(rooms_jsonb.roomData, '{ gameStatus }', ?::jsonb, TRUE)", roomId, roomData,
                gson.toJson(json), gson.toJson(json));
    }

    /**
     * Method to set total number of questions for game (which is part of the game status)
     * @param roomId - id of the room
     * @param questionNumber - number of questions
     */
    @Override
    public void setGameQuestionNumber(final String roomId, final int questionNumber) {
        String roomData = jdbcOperations.queryForObject("SELECT roomData FROM rooms_jsonb WHERE rooms_jsonb.id = ?",
                String.class,
                roomId);
        String gameStatus = jdbcOperations.queryForObject("SELECT roomData->'gameStatus' FROM rooms_jsonb WHERE " +
                        "rooms_jsonb.id = ?", String.class,
                roomId);

        JsonObject json = JsonParser.parseString(gameStatus).getAsJsonObject();
        json.addProperty("questionNumber", questionNumber);

        jdbcOperations.update("INSERT INTO rooms_jsonb (id, roomData) VALUES (?, " +
                "jsonb_set(?::jsonb, '{ gameStatus }', ?::jsonb, TRUE)) ON CONFLICT (id) DO UPDATE "
                + "SET roomData = jsonb_set(rooms_jsonb.roomData, '{ gameStatus }', ?::jsonb, TRUE)", roomId, roomData,
                gson.toJson(json), gson.toJson(json));
    }

    /**
     * Method to set current number of questions given to players (which is part of the game status)
     * @param roomId - id of the room
     * @param questionsCount - number of questions given to players
     */
    @Override
    public void setGameQuestionsCount(final String roomId, final int questionsCount) {
        String roomData = jdbcOperations.queryForObject("SELECT roomData FROM rooms_jsonb WHERE rooms_jsonb.id = ?",
                String.class,
                roomId);
        String gameStatus = jdbcOperations.queryForObject("SELECT roomData->'gameStatus' FROM rooms_jsonb WHERE " +
                        "rooms_jsonb.id = ?", String.class,
                roomId);

        JsonObject json = JsonParser.parseString(gameStatus).getAsJsonObject();
        json.addProperty("questionsCount", questionsCount);

        jdbcOperations.update("INSERT INTO rooms_jsonb (id, roomData) VALUES (?, " +
                "jsonb_set(?::jsonb, '{ gameStatus }', ?::jsonb, TRUE)) ON CONFLICT (id) DO UPDATE "
                + "SET roomData = jsonb_set(rooms_jsonb.roomData, '{ gameStatus }', ?::jsonb, TRUE)", roomId, roomData,
                gson.toJson(json), gson.toJson(json));
    }

    /**
     * Getter for total number of questions for game
     * @param roomId - room id
     * @return - number of questions for game
     */
    @Override
    public int getAmountOfQuestions(final String roomId) {
        try {
            return jdbcOperations.queryForObject("SELECT roomData->'gameStatus'->>'questionNumber' " +
                            "FROM rooms_jsonb WHERE id = ?", Integer.class,
                    roomId);
        } catch (NullPointerException exception) {
            return 0;
        }
    }

    /**
     * Getter for number of questions already given to players
     * @param roomId - id of the room
     * @return - number of questions given to players already
     */
    @Override
    public int getGameQuestionsCount(final String roomId) {
        try {
            return jdbcOperations.queryForObject("SELECT roomData->'gameStatus'->>'questionsCount' " +
                            "FROM rooms_jsonb WHERE id = ?", Integer.class,
                    roomId);
        } catch (NullPointerException exception) {
            return 0;
        }
    }

    /**
     * Getter of isQuestionAnswered value
     * @param roomId - room id
     * @param questionId - question id
     * @return map (roomId -> (questionId -> boolean value))
     */
    @Override
    public boolean getIsQuestionAnswered(final String roomId, final String questionId) {
        return Boolean.TRUE.equals(jdbcOperations.queryForObject("SELECT roomsQuestionsData->>'isAnswered' " +
                "FROM rooms_questions_jsonb WHERE "
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
    public void setQuestionScore(final String roomId, final String playerId, final String questionId,
                                 final int questionScore) {
        String playerStatsPerQuestion;

        try {
            playerStatsPerQuestion = jdbcOperations.queryForObject("SELECT playerStatsPerQuestion FROM " +
                            "players_stats_per_question_jsonb WHERE playerid = ? AND roomid = ? AND questionid = ?",
                    String.class,
                    playerId, roomId, questionId);
        } catch (EmptyResultDataAccessException exception) {
            playerStatsPerQuestion = "{}";
        }

        JsonObject json = JsonParser.parseString(playerStatsPerQuestion).getAsJsonObject();
        json.addProperty("questionScore", questionScore);

        jdbcOperations.update("INSERT INTO players_stats_per_question_jsonb (roomid, playerid, questionid, "
                + "playerStatsPerQuestion) VALUES (?, ?, ?, ?::jsonb) ON CONFLICT (roomid, playerid, questionid) DO " +
                "UPDATE SET playerStatsPerQuestion = ?::jsonb", roomId, playerId, questionId, gson.toJson(json),
                gson.toJson(json));
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
        return jdbcOperations.queryForObject("SELECT playerStatsPerQuestion->>'questionScore' FROM " +
                "players_stats_per_question_jsonb WHERE "
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
        JsonObject json = new JsonObject();
        json.addProperty("isAnswered", true);

        jdbcOperations.update("INSERT INTO rooms_questions_jsonb (roomid, questionid, roomsQuestionsData) " +
                "VALUES (?, ?, ?::jsonb) "
                + "ON CONFLICT (roomid, questionid) DO UPDATE SET roomsQuestionsData = ?::jsonb ", roomId, questionId,
                gson.toJson(json), gson.toJson(json));
    }

    /**
     * Method to get current game question
     * @param roomId - id of the room
     * @return - current game question
     */
    @Override
    public String getGameQuestion(final String roomId) {
        return jdbcOperations.queryForObject("SELECT roomData->'gameStatus'->>'questionId' FROM rooms_jsonb " +
                "WHERE id = ?", String.class, roomId);
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
        return Boolean.TRUE.equals(jdbcOperations.queryForObject("SELECT playerStatsPerQuestion->>'hasAnswered' FROM "
                        + "players_stats_per_question_jsonb WHERE roomid = ? AND playerid = ? AND questionid = ?",
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
            hasAnswered) {
        String playerStatsPerQuestion = jdbcOperations.queryForObject("SELECT playerStatsPerQuestion FROM " +
                        "players_stats_per_question_jsonb WHERE " +
                        "roomid = ? AND playerid = ? AND questionid = ?", String.class,
                roomId, playerId, questionId);

        JsonObject json = JsonParser.parseString(playerStatsPerQuestion).getAsJsonObject();
        json.addProperty("hasAnswered", hasAnswered);

        jdbcOperations.update("INSERT INTO players_stats_per_question_jsonb (roomid, playerid, questionid, "
                + "playerstatsperquestion) VALUES (?, ?, ?, ?::jsonb)" +
                "" +
                "ON CONFLICT (roomid, playerid, questionid) DO UPDATE SET "
                + "playerStatsPerQuestion = " +
                "?::jsonb",
                roomId, playerId, questionId, gson.toJson(json), gson.toJson(json));
    }

    /**
     * set question ids for certain game
     * @param roomId - id of the room
     * @param questionIdsPerGame - question ids to initialize in the game
     */
    @Override
    public void setQuestionIdsPerGame(final String roomId, final List<String> questionIdsPerGame) {
        boolean isAnswered = false;
        for (String questionId : questionIdsPerGame) {
            jdbcOperations.update("INSERT INTO rooms_questions_jsonb (roomid, questionid, roomsQuestionsData) " +
                    "VALUES (?, ?, ?::jsonb) ON " +
                    "CONFLICT (roomid, questionid) DO UPDATE SET roomsQuestionsData = ?::jsonb", roomId, questionId,
                    gson.toJson(isAnswered), gson.toJson(isAnswered));
        }
    }

    /**
     * get question ids from certain game
     * @param roomId - id of the room
     * @return - question ids
     */
    @Override
    public List<String> getQuestionIdsPerGame(final String roomId) {
        return jdbcOperations.query("SELECT rooms_questions_jsonb.questionid FROM rooms_questions_jsonb "
                + "WHERE rooms_questions_jsonb.roomid = ?", (resultSet, i) -> resultSet.getString(1), roomId);
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
        return jdbcOperations.queryForObject("SELECT rulesData->>'rules' FROM rules_jsonb", String.class);
    }
}
