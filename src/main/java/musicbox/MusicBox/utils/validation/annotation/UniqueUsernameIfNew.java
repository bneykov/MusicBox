package musicbox.MusicBox.utils.validation.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import musicbox.MusicBox.utils.validation.UniqueUsernameIfNewValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueUsernameIfNewValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsernameIfNew {
    String message() default "Account with this username already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
