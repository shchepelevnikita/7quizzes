package it.sevenbits.quizzes.web.controllers;

import it.sevenbits.quizzes.core.annotations.AuthRoleRequired;
import it.sevenbits.quizzes.core.credentials.IUserCredentials;
import it.sevenbits.quizzes.core.models.QuestionLocation;
import it.sevenbits.quizzes.core.models.Rules;
import it.sevenbits.quizzes.core.services.GameService;
import it.sevenbits.quizzes.web.models.AnswerQuestionRequest;
import it.sevenbits.quizzes.web.models.AnswerQuestionResponse;
import it.sevenbits.quizzes.web.models.GameStatus;
import it.sevenbits.quizzes.web.models.GetTotalScoresResponse;
import it.sevenbits.quizzes.web.models.QuestionWithOptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Game Controller - controller that keeps endpoints of main application
 * (deals with HTTP requests from client and sends him responses)
 * Intermediary between client and service
 */
@Controller
public class GameController {
    private final GameService gameService;

    /**
     * GameController constructor
     * @param gameService - instance of GameService that needs to be in there to access service
     */
    public GameController(final GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Endpoint for starting game (post-method)
     * @param roomId - id of the room in which the game will start
     * @param userCredentials - user credentials
     * @return - returns questionId
     */
    @AuthRoleRequired("USER")
    @PostMapping("/rooms/{roomId}/game/start")
    public ResponseEntity<QuestionLocation> startGame(@PathVariable final String roomId,
                                                      final IUserCredentials userCredentials) {
        QuestionLocation questionLocation = gameService.startGame(roomId, userCredentials.getUserId());
        return new ResponseEntity<>(questionLocation, HttpStatus.OK);
    }

    /**
     * Endpoint for getting question (get-method)
     * @param questionId - id of the question that we need to get
     * @return - returns questionId, its text and answer list
     */
    @AuthRoleRequired("USER")
    @GetMapping("/rooms/{roomId}/game/question/{questionId}")
    public ResponseEntity<QuestionWithOptions> getQuestion(@PathVariable final String questionId) {
        return new ResponseEntity<>(gameService.getQuestionWithOptions(questionId), HttpStatus.OK);
    }

    /**
     * Endpoint that is responsible for post-methods to answer questions
     * @param answer - DTO containing id of the answer
     * @param roomId - room id
     * @param questionId - question id
     * @param userCredentials - user credentials
     * @return - DTO: right answer, total score, question score, next question id
     */
    @AuthRoleRequired("USER")
    @PostMapping("/rooms/{roomId}/game/question/{questionId}/answer")
    public ResponseEntity<AnswerQuestionResponse> answerQuestion(@RequestBody final AnswerQuestionRequest answer,
                                                                 @PathVariable final String roomId,
                                                                 @PathVariable final String questionId,
                                                                 final IUserCredentials userCredentials) {
        AnswerQuestionResponse answerQuestionResponse = gameService.getAnswerQuestionResponse(answer,
                userCredentials.getUserId(), roomId, questionId);
        return new ResponseEntity<>(answerQuestionResponse, HttpStatus.OK);
    }

    /**
     * Get method for getting game status
     * @param roomId - id of the room
     * @return - status of the game in certain room
     */
    @AuthRoleRequired("USER")
    @GetMapping("/rooms/{roomId}/game")
    public ResponseEntity<GameStatus> getGameStatus(@PathVariable final String roomId) {
        return new ResponseEntity<>(gameService.getGameStatus(roomId), HttpStatus.OK);
    }

    /**
     * Get method for getting rules
     * @return rules
     */
    @GetMapping("/rules")
    public ResponseEntity<Rules> getRules() {
        Rules rules = new Rules(gameService.getRules());
        return new ResponseEntity<>(rules, HttpStatus.OK);
    }

    /**
     * Get method to get total scores of all the players in a certain room
     * @param roomId - id of a certain room
     * @return response entity with a list of models: each model contains player id, player name and player total score
     */
    @AuthRoleRequired("USER")
    @GetMapping("/rooms/{roomId}/game/totalscores")
    public ResponseEntity<List<GetTotalScoresResponse>> getTotalScores(@PathVariable final String roomId) {
        return new ResponseEntity<>(gameService.getTotalScores(roomId), HttpStatus.OK);
    }
}
