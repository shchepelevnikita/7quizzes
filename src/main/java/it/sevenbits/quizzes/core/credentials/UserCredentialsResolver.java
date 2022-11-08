package it.sevenbits.quizzes.core.credentials;

import it.sevenbits.quizzes.core.interceptors.JwtAuthInterceptor;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Class for resolving user credentials
 */
public class UserCredentialsResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType()
                .isAssignableFrom(IUserCredentials.class);
    }

    @Override
    public UserCredentials resolveArgument(
            @NonNull final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            @NonNull final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        try {
            return (UserCredentials) webRequest.getAttribute(
                    JwtAuthInterceptor.USER_CREDENTIALS,
                    RequestAttributes.SCOPE_REQUEST
            );
        } catch (Exception e) {
            return null;
        }
    }

}
