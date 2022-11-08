package it.sevenbits.quizzes.core.services;

import it.sevenbits.quizzes.core.encoders.BCryptPasswordEncoder;
import it.sevenbits.quizzes.core.exceptions.LoginFailedException;
import it.sevenbits.quizzes.core.exceptions.UserAlreadyExistsException;
import it.sevenbits.quizzes.core.models.User;
import it.sevenbits.quizzes.core.repositories.UserRepository.IUserRepository;
import it.sevenbits.quizzes.web.models.GetUsersResponse;
import it.sevenbits.quizzes.web.models.SignInRequest;
import it.sevenbits.quizzes.web.models.SignInResponse;
import it.sevenbits.quizzes.web.models.SignUpRequest;
import it.sevenbits.quizzes.web.models.GetUserResponse;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Service for working with users
 */
@Service
public class UserService {
    private final IUserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ITokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    /**
     * User Service constructor
     * @param userRepository - user repo
     * @param passwordEncoder - password encoder
     * @param tokenService - jwt token service
     * @param refreshTokenService - refresh token service
     */
    public UserService(final IUserRepository userRepository, final BCryptPasswordEncoder passwordEncoder,
                       final ITokenService tokenService, final RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
    }

    /**
     * Method for signing up
     * @param signUpRequest - request for sign up
     */
    public void signUp(final SignUpRequest signUpRequest) {
        String hashedPassword = passwordEncoder.hash(signUpRequest.getPassword());

        User user = userRepository.getUserByEmail(signUpRequest.getEmail());

        if (user != null) {
            throw new UserAlreadyExistsException("User '" + signUpRequest.getEmail() + "' already has an account !");
        }

        userRepository.createUser(signUpRequest.getEmail(), hashedPassword, Collections.singletonList("USER"));
    }

    /**
     * Method for signing in
     * @param signInRequest - request for sign in
     * @return - response for sign in
     */
    public SignInResponse signIn(final SignInRequest signInRequest) {
        User user = userRepository.getUserByEmail(signInRequest.getEmail());

        if (user == null) {
            throw new LoginFailedException("User '" + signInRequest.getEmail() + "' was not found !");
        }

        if (!passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())) {
            throw new LoginFailedException("Wrong password");
        }

        String accessToken = tokenService.createToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user.getUserId()).getToken();

        return new SignInResponse(accessToken, refreshToken);
    }

    /**
     * Method for getting all users
     * @return - response with all users
     */
    public GetUsersResponse getUsers() {
        List<User> users = userRepository.getUsers();
        return new GetUsersResponse(users);
    }

    /**
     * Method for getting user by user id
     * @param userId - user id
     * @return - response with user
     */
    public GetUserResponse getUser(final String userId) {
        User user = userRepository.getUserById(userId);
        return new GetUserResponse(user.getUserId(), user.getEmail(), user.getRoles());
    }
}
