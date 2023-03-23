package musicbox.MusicBox.utils.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import musicbox.MusicBox.utils.validation.ImageNotEmptyValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ImageNotEmptyValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageNotEmpty {
    String message() default "Please select an image";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
