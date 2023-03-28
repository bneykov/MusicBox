package musicbox.MusicBox.utils.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import musicbox.MusicBox.utils.validation.CustomSizeValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomSizeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomSize {
    String message() default "Password length must be at least 7 characters long.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
