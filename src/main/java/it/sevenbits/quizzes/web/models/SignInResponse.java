package it.sevenbits.quizzes.web.models;

/**
 * Response with jwt token after signing in
 */
public class SignInResponse {
    private final String accessToken;
    private final String refreshToken;

    /**
     * response constructor
     * @param accessToken - access token that allows user to be authorized
     * @param refreshToken - refresh token that allows to refresh accessToken
     */
    public SignInResponse(final String accessToken, final String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
