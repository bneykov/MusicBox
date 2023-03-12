package musicbox.MusicBox.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import musicbox.MusicBox.validation.PasswordMatcher;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = PasswordMatcher.class)
public @interface PasswordMatch {

    String password();

    String confirmPassword();

    String message() default "Passwords don't match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
