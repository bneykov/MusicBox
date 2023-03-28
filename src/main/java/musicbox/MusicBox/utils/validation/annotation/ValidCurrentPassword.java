package musicbox.MusicBox.utils.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import musicbox.MusicBox.utils.validation.CurrentPasswordValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CurrentPasswordValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCurrentPassword {
    String message() default "Wrong password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
