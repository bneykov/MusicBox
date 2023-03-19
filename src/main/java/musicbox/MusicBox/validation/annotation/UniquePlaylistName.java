package musicbox.MusicBox.validation.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import musicbox.MusicBox.validation.UniquePlaylistNameValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniquePlaylistNameValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniquePlaylistName {
    String message() default "You already have a playlist with this name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
