package it.sevenbits.quizzes.core.interceptors;

import it.sevenbits.quizzes.core.annotations.AuthRoleRequired;
import it.sevenbits.quizzes.core.credentials.IUserCredentials;
import it.sevenbits.quizzes.core.models.User;
import it.sevenbits.quizzes.core.services.ITokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Interceptor that intercepts requests and checks for authorization rights
 */
public class JwtAuthInterceptor implements HandlerInterceptor, ITokenService {
    /**
     * const for user credentials attribute
     */
    public static final String USER_CREDENTIALS = "userCredentialsAttr";
    private final ITokenService jwtService;

    /**
     * Interceptor's constructor
     * @param jwtService - jwt token service
     */
    public JwtAuthInterceptor(final ITokenService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Method that checks whether the user is authorized
     * @param request - servlet request itself
     * @param response - servlet response
     * @param handler - handler
     * @return - returns boolean value which tells whether the user is authorized or not
     */
    @Override
    public boolean preHandle(@NonNull final HttpServletRequest request,
                             @NonNull final HttpServletResponse response,
                             @NonNull final Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            return checkAuthorization(method, request, response);
        }
        return true;
    }

    /**
     * Method for extracting user credentials from jwt token in request's header
     * @param request - the request itself
     * @return - user credentials
     */
    public IUserCredentials getUserCredentials(final HttpServletRequest request) {
        try {
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (header == null || !header.startsWith("Bearer ")) {
                return null;
            }

            String token = header.substring(7);
            IUserCredentials credentials = jwtService.parseToken(token);
            request.setAttribute(USER_CREDENTIALS, credentials);
            return credentials;
        } catch (Exception e) {
            return null;
        }
    }

    private boolean checkAuthorization(
            final Method method,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        try {
            IUserCredentials userCredentials = getUserCredentials(request);

            if (method.isAnnotationPresent(AuthRoleRequired.class)) {
                if (userCredentials == null) {
                    response.sendError(
                            HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                    return false;
                }
                AuthRoleRequired annotation = method
                        .getAnnotation(AuthRoleRequired.class);
                String requiredRole = annotation.value();
                List<String> userRoles = new ArrayList<>();
                if (userCredentials.getRoles() != null) {
                    userRoles.addAll(userCredentials.getRoles());
                }
                boolean isAuthorized = userRoles.contains(requiredRole);
                if (!isAuthorized) {
                    response.sendError(
                            HttpServletResponse.SC_FORBIDDEN, "Not enough permissions");
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String createToken(User user) {
        return null;
    }

    @Override
    public IUserCredentials parseToken(String token) {
        return null;
    }
}
