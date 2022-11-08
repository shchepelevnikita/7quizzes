package it.sevenbits.quizzes.core.repositories.GameRepository;

import it.sevenbits.quizzes.core.models.Game;
import it.sevenbits.quizzes.web.models.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Repository that contains info about game states
 */
public class GameRepository implements IGameRepository {

    private final Map<String, Game> games;
    private final String rules = "Players are simultaneously asked a question and given 30 seconds to answer. " +
            "The answer is accepted by the player who first chose the answer option. There can be only one correct " +
            "answer. If the answer turned out to be correct, the answering player receives points, and the rest of " +
            "the players do not receive anything, while they see the correct answer and the respondent. If the " +
            "answer turned out to be incorrect, then the respondent does not receive an answer points and loses the " +
            "opportunity to choose an option in this round, waiting for the others to answer. The round ends when " +
            "one of the players gives the correct answer or the time allotted for the answer in this round runs out. " +
            "The game ends when the number of questions expires.";
    private final Random rand;

    /**
     * GameRepository constructor that initializes its maps
     */
    @Autowired
    public GameRepository() {
        this.games = new HashMap<>();
        this.rand = new Random();
    }

    /**
     * method to add the game to the room
     * @param roomId - id of the room
     * @param game - the game itself
     */
    public void addGame(final String roomId, final Game game) {
        games.put(roomId, game);
    }

    /**
     * Setter for total score of the game
     * @param roomId - id of the room
     * @param playerId - id of the player
     * @param value - value to set
     */
    @Override
    public void setTotalScore(final String roomId, final String playerId, final int value) {
        Game tempGame = games.get(roomId);
        tempGame.setTotalScore(playerId, value);
        games.put(roomId, tempGame);
    }

    /**
     * Getter for total score
     * @param roomId - id of the room
     * @param playerId - id of the player
     * @return - total score in the room
     */
    @Override
    public int getTotalScore(final String roomId, final String playerId) {
        return games.get(roomId).getTotalScore(playerId);
    }

    /**
     * There should be implementation of readiness setter for Collection Repository but as I am only using Postgres JSONB
     * repository I don't need such implementation thus I have no desire to implement it
     * @param roomId - id of the room
     * @param playerId - id of the player
     * @param value - isReady value to be updated
     */
    @Override
    public void setIsReady(final String roomId, final String playerId, final boolean value) {}

    /**
     * There should be implementation of readiness getter for Collection Repository but as I am only using Postgres JSONB
     * repository I don't need such implementation thus I have no desire to implement it
     * @param roomId - id of the room
     * @param playerId - id of the player
     */
    @Override
    public boolean getIsReady(final String roomId, final String playerId) {
        return false;
    }

    /**
     * Setter for boolean value that indicates whether the game has started or not
     * @param roomId - id of the room
     * @param value - whether the game has started or not
     */
    public void setIsGameStarted(final String roomId, final boolean value) {
        Game tempGame = games.get(roomId);
        tempGame.setGameStarted(value);
        games.put(roomId, tempGame);
    }

    /**
     * Getter for map isGameStarted which tells which game has started and which not
     * @param roomId - id of the room
     * @return - map string -> boolean isGameStarted
     */
    public boolean getIsGameStarted(final String roomId) {
        return games.get(roomId).isGameStarted();
    }

    /**
     * Method to set certain status for game in certain room
     * @param roomId - id of the room
     * @param status - status of the game
     */
    public void setGameStatus(final String roomId, final String status) {
        Game tempGame = games.get(roomId);
        tempGame.setGameStatus(status);
        games.put(roomId, tempGame);
    }

    /**
     * Method to get current status of the game in certain room
     * @param roomId - id of the room
     * @return - current status of the game in certain room
     */
    public GameStatus getGameStatus(final String roomId) {
        return games.get(roomId).getGameStatus();
    }

    /**
     * Method to set current game question (which is part of the game status)
     * @param roomId - id of the room
     * @param questionId - id of the current question
     */
    public void setGameQuestion(final String roomId, final String questionId) {
        Game tempGame = games.get(roomId);
        tempGame.setGameQuestion(questionId);
        games.put(roomId, tempGame);
    }

    /**
     * Method to get current game question
     * @param roomId - id of the room
     * @return - current game question
     */
    public String getGameQuestion(final String roomId) {
        return games.get(roomId).getGameQuestion();
    }

    /**
     * Method to set total number of questions for game (which is part of the game status)
     * @param roomId - id of the room
     * @param questionNumber - number of questions
     */
    public void setGameQuestionNumber(final String roomId, final int questionNumber) {
        Game tempGame = games.get(roomId);
        tempGame.setGameQuestionNumber(questionNumber);
        games.put(roomId, tempGame);
    }

    /**
     * Method to set current number of questions given to players (which is part of the game status)
     * @param roomId - id of the room
     * @param questionsCount - number of questions given to players
     */
    public void setGameQuestionsCount(final String roomId, final int questionsCount) {
        Game tempGame = games.get(roomId);
        tempGame.setGameQuestionsCount(questionsCount);
        games.put(roomId, tempGame);
    }

    /**
     * Getter for number of questions already given to players
     * @param roomId - id of the room
     * @return - number of questions given to players already
     */
    public int getGameQuestionsCount(final String roomId) {
        return games.get(roomId).getGameQuestionsCount();
    }

    /**
     * Getter for total number of questions for game
     * @param roomId - id of the room
     * @return - number of questions for game
     */
    public int getAmountOfQuestions(final String roomId) {
        return games.get(roomId).getAmountOfQuestions();
    }

    /**
     * getter of whether the question was answered
     * @param roomId - id of the room
     * @param questionId - id of the question
     * @return - boolean value of whether the question was answered
     */
    public boolean getIsQuestionAnswered(final String roomId, final String questionId) {
        return games.get(roomId).getIsQuestionAnswered(questionId);
    }


    /**
     * Method to set whether the question has been answered
     * @param roomId - id of the room
     * @param questionId - id of the question
     */
    public void setIsQuestionAnswered(final String roomId, final String questionId) {
        Game tempGame = games.get(roomId);
        tempGame.setIsQuestionAnswered(questionId, true);
        games.put(roomId, tempGame);
    }

    /**
     * setter for question score
     * @param roomId - id of the room
     * @param playerId - id of the player
     * @param questionId - id of the question
     * @param value - value to be set on score
     */
    public void setQuestionScore(final String roomId, final String playerId, final String questionId, final int value) {
        Game tempGame = games.get(roomId);
        tempGame.setQuestionScores(playerId, questionId, value);
        games.put(roomId, tempGame);
    }

    /**
     * getter for question score
     * @param roomId - id of the room
     * @param playerId - id of the player
     * @param questionId - id of the question
     * @return - returns question score
     */
    public int getQuestionScore(final String roomId, final String playerId, final String questionId) {
        return games.get(roomId).getQuestionScore(playerId, questionId);
    }

    /**
     * Method to get random id of questions
     * @param roomId - id of the room
     * @return - id of the random question from game pool
     */
    public String getRandomQuestionId(final String roomId) {
        List<String> questionIdList = games.get(roomId).getQuestionIdsPerGame();
        String tempQuestionId = questionIdList.get(rand.nextInt(questionIdList.size()));
        while (games.get(roomId).getIsQuestionAnswered(tempQuestionId)) {
            tempQuestionId = questionIdList.get(rand.nextInt(questionIdList.size()));
        }
        return tempQuestionId;
    }

    /**
     * Method to get hasPlayerAnswered map
     * @param roomId - id of the room
     * @param playerId - id of the player
     * @param questionId  - id of the question
     * @return - hasPlayerAnswered map
     */
    public boolean getHasPlayerAnswered(final String roomId, final String playerId, final String questionId) {
        return games.get(roomId).getHasPlayerAnswered(playerId, questionId);
    }

    /**
     * Method to set a value is hasPlayerAnswered map
     * @param roomId - id of the room
     * @param playerId - id of the player
     * @param questionId - id of the question
     * @param value - value of whether the player has answered given question or not
     */
    @Override
    public void setHasPlayerAnswered(final String roomId, final String playerId, final String questionId, final boolean value) {
        Game tempGame = games.get(roomId);
        tempGame.setHasPlayerAnswered(playerId, questionId, value);
        games.put(roomId, tempGame);
    }

    /**
     * set question ids for certain game
     * @param roomId - id of the room
     * @param questionIdsPerGame - question ids to initialize in the game
     */
    @Override
    public void setQuestionIdsPerGame(final String roomId, final List<String> questionIdsPerGame) {
        Game tempGame = games.get(roomId);
        tempGame.setQuestionIdsPerGame(questionIdsPerGame);
        games.put(roomId, tempGame);
    }

    /**
     * get question ids from certain game
     * @param roomId - id of the room
     * @return - question ids
     */
    public List<String> getQuestionIdsPerGame(final String roomId) {
        return games.get(roomId).getQuestionIdsPerGame();
    }

    /**
     * checks whether the game exists or not
     * @param roomId - id of the room
     * @return boolean value which tells us whether the game exist or not
     */
    public boolean isGameExisting(final String roomId) {
        return games.get(roomId) != null;
    }

    @Override
    public String getRules() {
        return rules;
    }
}
