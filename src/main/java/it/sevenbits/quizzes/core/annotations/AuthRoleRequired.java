package it.sevenbits.quizzes.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Custom annotation for checking authorization
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthRoleRequired {
    /**
     * Returns a role which is required to access the method
     * @return the role name
     */
    String value();
}
