package it.sevenbits.quizzes.web;

import it.sevenbits.quizzes.core.exceptions.AnswerSameQuestionTwiceException;
import it.sevenbits.quizzes.core.exceptions.AnswerWhenGameNotStartedException;
import it.sevenbits.quizzes.core.exceptions.GameAlreadyStartedException;
import it.sevenbits.quizzes.core.exceptions.GameStartForbiddenOutsideRoomException;
import it.sevenbits.quizzes.core.exceptions.LoginFailedException;
import it.sevenbits.quizzes.core.exceptions.PlayerAlreadyInsideRoomException;
import it.sevenbits.quizzes.core.exceptions.PlayerExitRoomBeforeJoinException;
import it.sevenbits.quizzes.core.exceptions.QuestionAlreadyAnsweredByOtherPlayerException;
import it.sevenbits.quizzes.core.exceptions.TokenRefreshException;
import it.sevenbits.quizzes.core.exceptions.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * class for handling exceptions
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * method which handles the answer same question twice exception
     * @param ex - exception
     * @return - response entity with status and message in its body
     */
    @ExceptionHandler(AnswerSameQuestionTwiceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleAnswerSameQuestionTwiceException(
            final AnswerSameQuestionTwiceException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    /**
     * method which handles the answer when game not started exception
     * @param ex - exception
     * @return - response entity with status and message in its body
     */
    @ExceptionHandler(AnswerWhenGameNotStartedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleAnswerWhenGameNotStartedException(
            final AnswerWhenGameNotStartedException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    /**
     * method which handles the game already started exception
     * @param ex - exception
     * @return - response entity with status and message in its body
     */
    @ExceptionHandler(GameAlreadyStartedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleGameAlreadyStartedException(
            final GameAlreadyStartedException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    /**
     * method which handles the game start forbidden outside room exception
     * @param ex - exception
     * @return - response entity with status and message in its body
     */
    @ExceptionHandler(GameStartForbiddenOutsideRoomException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleGameStartForbiddenOutsideRoomException(
            final GameStartForbiddenOutsideRoomException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    /**
     * method which handles the player already inside room exception
     * @param ex - exception
     * @return - response entity with status and message in its body
     */
    @ExceptionHandler(PlayerAlreadyInsideRoomException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handlePlayerAlreadyInsideRoomException(
            final PlayerAlreadyInsideRoomException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    /**
     * method which handles the player exit room before join exception
     * @param ex - exception
     * @return - response entity with status and message in its body
     */
    @ExceptionHandler(PlayerExitRoomBeforeJoinException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handlePlayerExitRoomBeforeJoinException(
            final PlayerExitRoomBeforeJoinException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    /**
     * method which handles the question already answered by other player exception
     * @param ex - exception
     * @return - response entity with status and message in its body
     */
    @ExceptionHandler(QuestionAlreadyAnsweredByOtherPlayerException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleQuestionAlreadyAnsweredByOtherPlayerException(
            final QuestionAlreadyAnsweredByOtherPlayerException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    /**
     * method whuch handles failed login situation
     * @param ex - exception
     * @return - response entity with with status and message in its body
     */
    @ExceptionHandler(LoginFailedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleLoginFailedException(
            final LoginFailedException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    /**
     * method that handles user already exists exception
     * @param ex - exception
     * @return - response entity with with status and message in its body
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleUserAlreadyExistsException(
            final UserAlreadyExistsException ex
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * method that handles token refresh exception
     * @param ex - exception
     * @return - response entity with with status and message in its body
     */
    @ExceptionHandler(TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleTokenRefreshException(
            final TokenRefreshException ex
    ) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
}
