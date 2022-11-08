package it.sevenbits.quizzes.core.models;

import it.sevenbits.quizzes.web.models.GameStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * model of a game
 */
public class Game {
    private boolean isGameStarted;
    private final Map<String, Integer> totalScores;
    private final GameStatus gameStatus;
    private List<String> questionIdsPerGame;
    private final Map<String, Boolean> isQuestionAnswered;
    private final Map<String, Map<String, Integer>> questionScores;
    private final Map<String, Map<String, Boolean>> hasPlayerAnswered;
    private final int amountOfQuestions;

    /**
     * game constructor
     * @param playersList - list of players
     * @param amountOfQuestions - amount of questions
     * @param questionIdsPerGame - question ids of the game
     */
    public Game(final List<Player> playersList, final int amountOfQuestions, final List<String> questionIdsPerGame) {
        this.amountOfQuestions = amountOfQuestions;
        this.questionIdsPerGame = questionIdsPerGame;
        this.isGameStarted = false;
        this.gameStatus = new GameStatus("has not started", "", 0, amountOfQuestions);
        this.totalScores = new HashMap<>();
        this.isQuestionAnswered = new HashMap<>();
        this.hasPlayerAnswered = new HashMap<>();
        this.questionScores = new HashMap<>();
        Map<String, Integer> tempInnerQuestionScoresMap = new HashMap<>();
        Map<String, Boolean> tempInnerHasPlayerAnsweredMap = new HashMap<>();
        for (String questionId : questionIdsPerGame) {
            isQuestionAnswered.put(questionId, false);
            tempInnerQuestionScoresMap.put(questionId, 0);
            tempInnerHasPlayerAnsweredMap.put(questionId, false);
        }
        for (Player player : playersList) {
            totalScores.put(player.getPlayerId(), 0);
            questionScores.put(player.getPlayerId(), new HashMap<>(tempInnerQuestionScoresMap));
            hasPlayerAnswered.put(player.getPlayerId(), new HashMap<>(tempInnerHasPlayerAnsweredMap));
        }
    }

    /**
     * Getter for map isGameStarted which tells which game has started and which not
     * @return - map string -> boolean isGameStarted
     */
    public boolean isGameStarted() {
        return isGameStarted;
    }

    /**
     * setter for isGameStarted
     * @param gameStarted - value to be set
     */
    public void setGameStarted(final boolean gameStarted) {
        isGameStarted = gameStarted;
    }

    /**
     * getter for total score
     * @param playerId - id of the player
     * @return - total score
     */
    public int getTotalScore(final String playerId) {
        return totalScores.getOrDefault(playerId, 0);
    }

    /**
     * setter for total score
     * @param playerId - id of the player
     * @param totalScore - total score to be updated
     */
    public void setTotalScore(final String playerId, final int totalScore) {
        totalScores.put(playerId, totalScore);
    }

    /**
     * Method to get current status of the game in certain room
     * @return - current status of the game in certain room
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * Method to set certain status for game in certain room
     * @param status - status of the game
     */
    public void setGameStatus(final String status) {
        gameStatus.setStatus(status);
    }

    /**
     * Method to get current game question
     * @return - current game question
     */
    public String getGameQuestion() {
        return gameStatus.getQuestionId();
    }

    /**
     * Method to set current game question (which is part of the game status)
     * @param questionId - id of the current question
     */
    public void setGameQuestion(final String questionId) {
        this.gameStatus.setQuestionId(questionId);
    }

    /**
     * Getter for number of questions already given to players
     * @return - number of questions given to players already
     */
    public int getGameQuestionsCount() {
        return gameStatus.getQuestionsCount();
    }

    /**
     * Method to set current number of questions given to players (which is part of the game status)
     * @param questionsCount - number of questions given to players
     */
    public void setGameQuestionsCount(final int questionsCount) {
        gameStatus.setQuestionsCount(questionsCount);
    }

    /**
     * Method to set total number of questions for game (which is part of the game status)
     * @param questionNumber - number of questions
     */
    public void setGameQuestionNumber(final int questionNumber) {
        gameStatus.setQuestionNumber(questionNumber);
    }

    /**
     * get question ids from certain game
     * @return - question ids
     */
    public List<String> getQuestionIdsPerGame() {
        return questionIdsPerGame;
    }

    /**
     * set question ids for certain game
     * @param questionIdsPerGame - question ids to initialize in the game
     */
    public void setQuestionIdsPerGame(final List<String> questionIdsPerGame) {
        this.questionIdsPerGame = questionIdsPerGame;
    }

    /**
     * Getter of isQuestionAnswered value
     * @param questionId - question id
     * @return map (roomId -> (questionId -> boolean value))
     */
    public boolean getIsQuestionAnswered(final String questionId) {
        return isQuestionAnswered.get(questionId);
    }

    /**
     * Method to set whether the question has been answered
     * @param questionId - id of the question
     * @param value - boolean value which tells us whether the question has been answered
     */
    public void setIsQuestionAnswered(final String questionId, final boolean value) {
        this.isQuestionAnswered.put(questionId, value);
    }

    /**
     * Method for getting question score
     * @param playerId - id of the player
     * @param questionId - id of the question
     * @return - question score for certain room, player and question
     */
    public int getQuestionScore(final String playerId, final String questionId) {
        return questionScores.get(playerId).get(questionId);
    }

    /**
     * Method for setting question score
     * @param playerId - id of the player
     * @param questionId - id of the question
     * @param questionScore - score of the question
     */
    public void setQuestionScores(final String playerId, final String questionId, final int questionScore) {
        this.questionScores.get(playerId).put(questionId, questionScore);
    }

    /**
     * Method to get hasPlayerAnswered value
     * @param questionId - id of the question
     * @param playerId - id of the player
     * @return - hasPlayerAnswered map
     */
    public boolean getHasPlayerAnswered(final String playerId, final String questionId) {
        return hasPlayerAnswered.get(playerId).getOrDefault(questionId, false);
    }

    /**
     * Method to set a value in hasPlayerAnswered map
     * @param playerId - id of the player
     * @param questionId - id of the question
     * @param hasAnswered - a boolean value which tells us whether the player has answered or not
     */
    public void setHasPlayerAnswered(final String playerId, final String questionId, final boolean hasAnswered) {
        this.hasPlayerAnswered.get(playerId).put(questionId, hasAnswered);
    }

    /**
     * Getter for total number of questions for game
     * @return - number of questions for game
     */
    public int getAmountOfQuestions() {
        return amountOfQuestions;
    }
}
