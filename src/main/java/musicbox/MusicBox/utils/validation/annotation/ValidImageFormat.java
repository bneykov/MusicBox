package musicbox.MusicBox.utils.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import musicbox.MusicBox.utils.validation.ImageFormatValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ImageFormatValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidImageFormat {
    String message() default "Unsupported file format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
