package it.sevenbits.quizzes.core.services;

import it.sevenbits.quizzes.core.exceptions.AnswerSameQuestionTwiceException;
import it.sevenbits.quizzes.core.exceptions.AnswerWhenGameNotStartedException;
import it.sevenbits.quizzes.core.exceptions.GameAlreadyStartedException;
import it.sevenbits.quizzes.core.exceptions.GameStartForbiddenOutsideRoomException;
import it.sevenbits.quizzes.core.exceptions.QuestionAlreadyAnsweredByOtherPlayerException;
import it.sevenbits.quizzes.core.models.Game;
import it.sevenbits.quizzes.core.models.Player;
import it.sevenbits.quizzes.core.models.Question;
import it.sevenbits.quizzes.core.models.QuestionLocation;
import it.sevenbits.quizzes.core.models.Room;
import it.sevenbits.quizzes.core.repositories.GameRepository.IGameRepository;
import it.sevenbits.quizzes.core.repositories.QuestionRepository.IQuestionRepository;
import it.sevenbits.quizzes.core.repositories.RoomRepository.IRoomRepository;
import it.sevenbits.quizzes.core.repositories.UserRepository.IUserRepository;
import it.sevenbits.quizzes.web.models.AnswerQuestionRequest;
import it.sevenbits.quizzes.web.models.GetTotalScoresResponse;
import it.sevenbits.quizzes.web.models.QuestionWithOptions;
import it.sevenbits.quizzes.web.models.GameStatus;
import it.sevenbits.quizzes.web.models.AnswerQuestionResponse;
import it.sevenbits.quizzes.web.models.WSMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.gson.Gson;

/**
 * GameService - service that contains business logic and makes decisions
 */
@Service
public class GameService {
    private final IGameRepository gameRepository;
    private final IQuestionRepository questionRepository;
    private final IRoomRepository roomRepository;
    private final IUserRepository userRepository;
    private int amountOfQuestions;
    private final Gson gson;
    @Autowired
    private SimpMessagingTemplate template;

    /**
     * GameService constructor that receives instances of repositories to work with
     * @param gameRepository - instance of game repo as service works with it directly
     * @param questionRepository - instance of question repo as service works with it directly
     * @param roomRepository - instance of room repo as service works with it directly
     * @param userRepository - instance of user repo as service works with it directly through DI
     */
    @Autowired
    public GameService(final IGameRepository gameRepository, final IQuestionRepository questionRepository,
                       final IRoomRepository roomRepository, final IUserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.questionRepository = questionRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.gson = new Gson();
    }

    /**
     * Method that addresses question repo and asks to get random id of given questions
     * @return - random id of some question from the repo
     */
    private String chooseQuestionId(final String roomId) {
        String randomQuestionId = gameRepository.getRandomQuestionId(roomId);
        return randomQuestionId;
    }

    private List<String> chooseRandomQuestionIdPoolForGame() {
        List<String> randomQuestionIdList = new ArrayList<>();
        for (int i = 0; i < amountOfQuestions; i++) {
            randomQuestionIdList.add(questionRepository.getRandomQuestionId(randomQuestionIdList));
        }
        return randomQuestionIdList;
    }

    /**
     * Method that allows to get question with text and answers by id
     * @param questionId - id of the question
     * @return - DTO with above-mentioned fields
     */
    public QuestionWithOptions getQuestionWithOptions(final String questionId) {
        Question question = questionRepository.getQuestion(questionId);
        return new QuestionWithOptions(questionId, question.getQuestionText(), question.getAnswersList());
    }

    /**
     * Method that allows to start the game
     * @param roomId - id of the room to start the game in
     * @param userId - user id
     * @return - questionLocation model with id of the first question
     */
    public QuestionLocation startGame(final String roomId, final String userId) {
        this.amountOfQuestions = 3;
        if (gameRepository.isGameExisting(roomId) && gameRepository.getIsGameStarted(roomId)) {
            throw new GameAlreadyStartedException("The game has already begun !");
        }
        if (!roomRepository.getRoom(roomId).getPlayersIds().contains(userId)) {
            throw new GameStartForbiddenOutsideRoomException("You have to join the room to start the game !");
        }

        gameRepository.setIsReady(roomId, userId, true);

        boolean everyoneReady = true;

        for (String playerId: roomRepository.getRoom(roomId).getPlayersIds()) {
            if (!gameRepository.getIsReady(roomId, playerId)) {
                everyoneReady = false;
            }
        }

        QuestionLocation questionLocation = new QuestionLocation();

        if (everyoneReady) {
            List<String> questionIds = chooseRandomQuestionIdPoolForGame();
            Game game = new Game(roomRepository.getRoom(roomId).getPlayers(), amountOfQuestions, questionIds);
            gameRepository.setQuestionIdsPerGame(roomId, questionIds);
            gameRepository.addGame(roomId, game);
            gameRepository.setGameStatus(roomId, "in process");
            gameRepository.setIsGameStarted(roomId, true);
            questionLocation = new QuestionLocation(chooseQuestionId(roomId));
            gameRepository.setGameQuestionNumber(roomId, amountOfQuestions);
            gameRepository.setGameQuestionsCount(roomId, 1);
            gameRepository.setGameQuestion(roomId, questionLocation.getQuestionId());

            for (String playerId : roomRepository.getRoom(roomId).getPlayersIds()) {
                for (String questionId : questionIds) {
                    gameRepository.setQuestionScore(roomId, playerId, questionId, 0);
                    gameRepository.setHasPlayerAnswered(roomId, playerId, questionId, false);
                }
            }

            WSMessage message = new WSMessage("gameStarted", questionLocation.getQuestionId());

            template.convertAndSend("/topic/game",  message);
        }

        return questionLocation;
    }

    /**
     * private method that checks whether the client's answer is correct
     */
    private boolean validateAnswer(final Question question, final AnswerQuestionRequest answer) {
        return Objects.equals(question.getCorrectAnswerId(), answer.getAnswerId());
    }

    private void updateStats(final String roomId, final Question question) {
        gameRepository.setIsQuestionAnswered(roomId, question.getQuestionId());
        gameRepository.setGameQuestionsCount(roomId, gameRepository.getGameQuestionsCount(roomId) + 1);
        if (gameRepository.getGameQuestionsCount(roomId) > gameRepository.getAmountOfQuestions(roomId)) {
            gameRepository.setGameStatus(roomId, "finished");
            gameRepository.setIsGameStarted(roomId, false);
            gameRepository.setGameQuestion(roomId, "");
            gameRepository.setGameQuestionsCount(roomId, gameRepository.getGameQuestionsCount(roomId) - 1);
            for (String playerId: roomRepository.getRoom(roomId).getPlayersIds()) {
                gameRepository.setIsReady(roomId, playerId, false);
                for (String questionId : gameRepository.getQuestionIdsPerGame(roomId)) {
                    gameRepository.setQuestionScore(roomId, playerId, questionId, 0);
                    gameRepository.setHasPlayerAnswered(roomId, playerId, questionId, false);
                }
            }
        } else {
            gameRepository.setGameQuestion(roomId, chooseQuestionId(roomId));
        }
    }

    /**
     * returns entity that shows after the player has answered the question
     * @param answer - request model containing answerId and playerId
     * @param userId - user id
     * @param roomId - room id
     * @param questionId - question id
     * @return response entity that contains id of correct answer, next question id, total score and question score
     */
    public AnswerQuestionResponse getAnswerQuestionResponse(final AnswerQuestionRequest answer, final String userId,
                                                            final String roomId, final String questionId) {
        Question question = questionRepository.getQuestion(questionId);

        if (!gameRepository.getIsGameStarted(roomId)) {
            throw new AnswerWhenGameNotStartedException("You can not answer questions before  game started !");
        }
        if (gameRepository.getHasPlayerAnswered(roomId, userId, question.getQuestionId())) {
            throw new AnswerSameQuestionTwiceException("You can not answer the same question twice !");
        }
        if (gameRepository.getIsQuestionAnswered(roomId, question.getQuestionId())) {
            throw new QuestionAlreadyAnsweredByOtherPlayerException("The question has already been answered by other player !");
        }
        gameRepository.setHasPlayerAnswered(roomId, userId, question.getQuestionId(), true);
        boolean hasEveryoneAnswered = true;
        for (Player player : roomRepository.getRoom(roomId).getPlayers()) {
            if (!gameRepository.getHasPlayerAnswered(roomId, player.getPlayerId(), question.getQuestionId())
                    ) {
                hasEveryoneAnswered = false;
            }
        }
        boolean isAnswerCorrect = validateAnswer(question, answer);
        if (isAnswerCorrect) {
            gameRepository.setTotalScore(roomId, userId,
                    gameRepository.getTotalScore(roomId, userId) + question.getQuestionScore());
            gameRepository.setQuestionScore(roomId, userId, question.getQuestionId(), question.getQuestionScore());
        }
        if (hasEveryoneAnswered) {
            updateStats(roomId, question);
            AnswerQuestionResponse res = new AnswerQuestionResponse(question.getCorrectAnswerId(), gameRepository.getGameQuestion(roomId),
                    gameRepository.getTotalScore(roomId, userId),
                gameRepository.getQuestionScore(roomId, userId, question.getQuestionId()));
            WSMessage message = new WSMessage("nextQuestion", gson.toJson(res));
            template.convertAndSend("/topic/game",  message);
        }

        return new AnswerQuestionResponse(question.getCorrectAnswerId(), gameRepository.getGameQuestion(roomId),
                    gameRepository.getTotalScore(roomId, userId),
                gameRepository.getQuestionScore(roomId, userId, question.getQuestionId()));
    }

    /**
     * Getter for game status
     * @param roomIdValue - id of the room
     * @return - game status (current question, total number of questions, status,
     * number of questions given to players)
     */
    public GameStatus getGameStatus(final String roomIdValue) {
        return gameRepository.getGameStatus(roomIdValue);
    }

    public String getRules() {
        return gameRepository.getRules();
    }

    /**
     * Getter for total scores
     * @param roomId - id of a certain room
     * @return list of models: each model contains player id, player name and player total score
     */
    public List<GetTotalScoresResponse> getTotalScores(final String roomId) {
        Room room = roomRepository.getRoom(roomId);
        List<GetTotalScoresResponse> totalScoresList = new ArrayList<>();

        for (String playerId: room.getPlayersIds()) {
            String playerName = userRepository.getUserById(playerId).getEmail();
            int totalScore = gameRepository.getTotalScore(roomId, playerId);
            totalScoresList.add(new GetTotalScoresResponse(playerId, playerName, totalScore));
        }

        return totalScoresList;
    }
}
