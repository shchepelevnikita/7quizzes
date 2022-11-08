package it.sevenbits.quizzes.web.controllers;

import it.sevenbits.quizzes.core.annotations.AuthRoleRequired;
import it.sevenbits.quizzes.core.credentials.IUserCredentials;
import it.sevenbits.quizzes.core.exceptions.TokenRefreshException;
import it.sevenbits.quizzes.core.models.User;
import it.sevenbits.quizzes.core.services.ITokenService;
import it.sevenbits.quizzes.core.services.RefreshTokenService;
import it.sevenbits.quizzes.core.services.UserService;
import it.sevenbits.quizzes.web.models.GetUsersResponse;
import it.sevenbits.quizzes.web.models.RefreshTokenRequest;
import it.sevenbits.quizzes.web.models.SignInRequest;
import it.sevenbits.quizzes.web.models.SignInResponse;
import it.sevenbits.quizzes.web.models.SignUpRequest;
import it.sevenbits.quizzes.web.models.GetUserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Controller for users
 */
@Controller
public class UserController {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final ITokenService jwtService;

    /**
     * User controller's constructor
     * @param userService - user service
     * @param refreshTokenService - refresh token service
     * @param jwtService - jwt service
     */
    public UserController(final UserService userService,
                          final RefreshTokenService refreshTokenService,
                          final ITokenService jwtService) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    /**
     * Method for signing up
     * @param signUpRequest - request for sign up
     * @return - response entity with ok status
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody final SignUpRequest signUpRequest) {
        userService.signUp(signUpRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Method for signing in
     * @param signInRequest - request for sign in
     * @return - response entity with sign in response
     */
    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> signIn(@RequestBody final SignInRequest signInRequest) {
        SignInResponse response = userService.signIn(signInRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Method for getting all users (admin only)
     * @return - list of all users
     */
    @AuthRoleRequired("ADMIN")
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        GetUsersResponse response = userService.getUsers();
        return new ResponseEntity<>(response.getUsers(), HttpStatus.OK);
    }

    /**
     * Method for getting user by user id (admin only)
     * @param userId - user id
     * @return - response with user
     */
    @AuthRoleRequired("ADMIN")
    @GetMapping("/users/{userId}")
    public ResponseEntity<GetUserResponse> getUser(@PathVariable final String userId) {
        GetUserResponse response = userService.getUser(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Method that is used by user to see his email, id and roles
     * @param userCredentials - user creds
     * @return - response entity containing user data
     */
    @AuthRoleRequired("USER")
    @GetMapping("/whoami")
    public ResponseEntity<GetUserResponse> whoAmI(final IUserCredentials userCredentials) {
        GetUserResponse response = userService.getUser(userCredentials.getUserId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Post method to refresh the tokens
     * @param request - request with refresh token
     * @return - new access token and refresh token also, may throw exception if there is no such refresh token in db
     */
    @AuthRoleRequired("USER")
    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@RequestBody final RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(user -> {
                    String token = jwtService.createToken(user);
                    return ResponseEntity.ok(new SignInResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }
}
