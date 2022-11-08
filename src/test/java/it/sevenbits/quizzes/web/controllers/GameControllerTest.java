package it.sevenbits.quizzes.web.controllers;

import it.sevenbits.quizzes.core.credentials.IUserCredentials;
import it.sevenbits.quizzes.core.models.QuestionLocation;
import it.sevenbits.quizzes.core.services.GameService;
import it.sevenbits.quizzes.web.models.AnswerQuestionRequest;
import it.sevenbits.quizzes.web.models.AnswerQuestionResponse;
import it.sevenbits.quizzes.web.models.GameStatus;
import it.sevenbits.quizzes.web.models.QuestionWithOptions;
import it.sevenbits.quizzes.web.models.StartGameRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GameControllerTest {
    private GameService mockGameService;
    private GameController gameController;

    @BeforeEach
    public void setup() {
        mockGameService = mock(GameService.class);
        gameController = new GameController(mockGameService);
    }

    @Test
    public void testStartGame() {
        String roomId = "1";
        String userId = "userId";
        IUserCredentials userCredentials = mock(IUserCredentials.class);
        when(userCredentials.getUserId()).thenReturn(userId);
        QuestionLocation mockQuestionLocation = mock(QuestionLocation.class);
        when(mockGameService.startGame(roomId, userId)).thenReturn(mockQuestionLocation);
        ResponseEntity<QuestionLocation> answer = gameController.startGame(roomId, userCredentials);
        verify(mockGameService, times(1)).startGame(roomId, userId);
        Assertions.assertEquals(HttpStatus.OK, answer.getStatusCode());
        Assertions.assertEquals(mockQuestionLocation, answer.getBody());
    }

    @Test
    public void testGetQuestion() {
        String questionId = "1";
        QuestionWithOptions questionMock = mock(QuestionWithOptions.class);
        when(mockGameService.getQuestionWithOptions(questionId)).thenReturn(questionMock);
        ResponseEntity<QuestionWithOptions> answer = gameController.getQuestion(questionId);
        verify(mockGameService, times(1)).getQuestionWithOptions(questionId);
        Assertions.assertEquals(HttpStatus.OK, answer.getStatusCode());
        Assertions.assertEquals(questionMock, answer.getBody());
    }

    @Test
    public void testGetGameStatus() {
        GameStatus mockGameStatus = mock(GameStatus.class);
        String roomId = "1";
        when(mockGameService.getGameStatus(roomId)).thenReturn(mockGameStatus);
        ResponseEntity<GameStatus> answer = gameController.getGameStatus(roomId);
        verify(mockGameService, times(1)).getGameStatus(roomId);
        Assertions.assertEquals(HttpStatus.OK, answer.getStatusCode());
        Assertions.assertEquals(mockGameStatus, answer.getBody());
    }

    @Test
    public void testAnswerQuestion() {
        AnswerQuestionRequest mockAnswerRequest = mock(AnswerQuestionRequest.class);
        AnswerQuestionResponse mockAnswerResponse = mock(AnswerQuestionResponse.class);
        String userId = "userId";
        String roomId = "roomId";
        String questionId = "questionId";
        IUserCredentials userCredentials = mock(IUserCredentials.class);
        when(userCredentials.getUserId()).thenReturn(userId);
        when(mockGameService.getAnswerQuestionResponse(mockAnswerRequest, userId, roomId, questionId)).thenReturn(mockAnswerResponse);
        ResponseEntity<AnswerQuestionResponse> answer = gameController.answerQuestion(mockAnswerRequest, roomId, questionId, userCredentials);
        verify(mockGameService, times(1)).getAnswerQuestionResponse(mockAnswerRequest, userId, roomId, questionId);
        Assertions.assertEquals(HttpStatus.OK, answer.getStatusCode());
        Assertions.assertEquals(mockAnswerResponse, answer.getBody());
    }
}
