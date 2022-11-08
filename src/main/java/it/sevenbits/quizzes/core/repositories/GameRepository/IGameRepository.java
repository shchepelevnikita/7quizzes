package it.sevenbits.quizzes.core.repositories.GameRepository;

import it.sevenbits.quizzes.core.models.Game;
import it.sevenbits.quizzes.web.models.GameStatus;

import java.util.List;

/**
 * GameRepository interface (cause there will be more implementations)
 */
public interface IGameRepository {
    /**
     * method to add the game to the room
     * @param roomId - id of the room
     * @param game - the game itself
     */
    void addGame(String roomId, Game game);

    /**
     * setter for isGameStarted
     * @param roomId - id of the room
     * @param value - value to be set
     */
    void setIsGameStarted(String roomId, boolean value);

    /**
     * Getter for map isGameStarted which tells which game has started and which not
     * @param roomId - room id
     * @return - map string -> boolean isGameStarted
     */
    boolean getIsGameStarted(String roomId);

    /**
     * setter for total score
     * @param roomId - id of the room
     * @param playerId - id of the player
     * @param value - total score to be updated
     */
    void setTotalScore(String roomId, String playerId, int value);

    /**
     * getter for total score
     * @param roomId - id of the room
     * @param playerId - id of the player
     * @return - total score
     */
    int getTotalScore(String roomId, String playerId);

    /**
     * setter for isReady
     * @param roomId - id of the room
     * @param playerId - id of the player
     * @param value - isReady value to be updated
     */
    void setIsReady(String roomId, String playerId, boolean value);

    /**
     * getter for isReady
     * @param roomId - id of the room
     * @param playerId - id of the player
     * @return - isReady value
     */
    boolean getIsReady(String roomId, String playerId);

    /**
     * Method to get current status of the game in certain room
     * @param roomId - id of the room
     * @return - current status of the game in certain room
     */
    GameStatus getGameStatus(String roomId);

    /**
     * Method to set certain status for game in certain room
     * @param roomId - id of the room
     * @param status - status of the game
     */
    void setGameStatus(String roomId, String status);

    /**
     * Method to set current game question (which is part of the game status)
     * @param roomId - id of the room
     * @param questionId - id of the current question
     */
    void setGameQuestion(String roomId, String questionId);

    /**
     * Method to set total number of questions for game (which is part of the game status)
     * @param roomId - id of the room
     * @param questionNumber - number of questions
     */
    void setGameQuestionNumber(String roomId, int questionNumber);

    /**
     * Method to set current number of questions given to players (which is part of the game status)
     * @param roomId - id of the room
     * @param questionsCount - number of questions given to players
     */
    void setGameQuestionsCount(String roomId, int questionsCount);

    /**
     * Getter for total number of questions for game
     * @param roomId - room id
     * @return - number of questions for game
     */
    int getAmountOfQuestions(String roomId);

    /**
     * Getter for number of questions already given to players
     * @param roomId - id of the room
     * @return - number of questions given to players already
     */
    int getGameQuestionsCount(String roomId);

    /**
     * Getter of isQuestionAnswered map
     * @param roomId - room id
     * @param questionId - question id
     * @return map (roomId -> (questionId -> boolean value))
     */
    boolean getIsQuestionAnswered(String roomId, String questionId);

    /**
     * Method for setting question score
     * @param roomId - id of the room
     * @param playerId - id of the player
     * @param questionId - id of the question
     * @param value - value of score to set
     */
    void setQuestionScore(final String roomId, final String playerId, final String questionId, final int value);

    /**
     * Method for getting question score
     * @param roomId - id of the room
     * @param playerId - id of the player
     * @param questionId - id of the question
     * @return - question score for certain room, player and question
     */
    int getQuestionScore(final String roomId, final String playerId, final String questionId);

    /**
     * Method to get random question id of those which belong to certain game
     * @param roomId - id of the room
     * @return - random question id
     */
    String getRandomQuestionId(final String roomId);

    /**
     * Method to set whether the question has been answered
     * @param roomId - id of the room
     * @param questionId - id of the question
     */
    void setIsQuestionAnswered(final String roomId, final String questionId);

    /**
     * Method to get current game question
     * @param roomId - id of the room
     * @return - current game question
     */
    String getGameQuestion(final String roomId);

    /**
     * Method to get hasPlayerAnswered map
     * @param roomId - room id
     * @param questionId - id of the question
     * @param playerId - id of the player
     * @return - hasPlayerAnswered map
     */
    boolean getHasPlayerAnswered(String roomId, String playerId, String questionId);

    /**
     * Method to set a value is hasPlayerAnswered map
     * @param roomId - id of the room
     * @param playerId - id of the player
     * @param questionId - id of the question
     * @param value - value of whether the player has answered given question or not
     */
    void setHasPlayerAnswered(final String roomId, final String playerId, final String questionId, final boolean value);

    /**
     * set question ids for certain game
     * @param roomId - id of the room
     * @param questionIdsPerGame - question ids to initialize in the game
     */
    void setQuestionIdsPerGame(String roomId, List<String> questionIdsPerGame);

    /**
     * get question ids from certain game
     * @param roomId - id of the room
     * @return - question ids
     */
    List<String> getQuestionIdsPerGame(String roomId);

    /**
     * checks whether the game exists or not
     * @param roomId - id of the room
     * @return boolean value which tells us whether the game exist or not
     */
    boolean isGameExisting(String roomId);

    /**
     * Method to get rules
     * @return rules
     */
    String getRules();
}
