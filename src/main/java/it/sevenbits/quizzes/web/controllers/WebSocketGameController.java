package it.sevenbits.quizzes.web.controllers;

import it.sevenbits.quizzes.core.credentials.IUserCredentials;
import it.sevenbits.quizzes.core.models.QuestionLocation;
import it.sevenbits.quizzes.core.services.GameService;
import it.sevenbits.quizzes.web.models.AnswerQuestionRequest;
import it.sevenbits.quizzes.web.models.AnswerQuestionResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Web Socket Game Controller
 */
@Controller
@MessageMapping("ws/rooms/{roomId}/game")
public class WebSocketGameController {
    private final GameService gameService;

    /**
     * Controller constructor
     * @param gameService - game service that is injected into this controller
     */
    public WebSocketGameController(final GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Start game method
     * @param roomId - room id
     * @param userCredentials - user credentials
     * @return question location
     */
    @MessageMapping("/start")
    @SendTo("/topic/game")
    public QuestionLocation startGame(@PathVariable final String roomId,
                                                      final IUserCredentials userCredentials) {
        return gameService.startGame(roomId, userCredentials.getUserId());
    }

    /**
     * method for answering question
     * @param roomId room id
     * @param questionId question id
     * @param userCredentials user credentials
     * @param answer request for answering question
     * @return answer response
     */
    @MessageMapping("/question/{questionId}/answer")
    @SendTo("/topic/game")
    public AnswerQuestionResponse answerQuestion(@RequestBody final AnswerQuestionRequest answer,
                                                                 @PathVariable final String roomId,
                                                                 @PathVariable final String questionId,
                                                                 final IUserCredentials userCredentials) {
        return gameService.getAnswerQuestionResponse(answer,
                userCredentials.getUserId(), roomId, questionId);
    }
}
