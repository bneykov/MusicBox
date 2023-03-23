package musicbox.MusicBox.utils.validation.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import musicbox.MusicBox.utils.validation.UniqueArtistNameValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueArtistNameValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueArtistName {
    String message() default "Artist with this name already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
