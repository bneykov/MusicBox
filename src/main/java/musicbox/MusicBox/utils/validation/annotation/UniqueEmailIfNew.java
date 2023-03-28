package musicbox.MusicBox.utils.validation.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import musicbox.MusicBox.utils.validation.UniqueEmailIfNewValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueEmailIfNewValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmailIfNew {
    String message() default "Account with this email already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
