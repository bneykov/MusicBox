package musicbox.MusicBox.utils.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import musicbox.MusicBox.utils.validation.annotation.CustomSize;

public class CustomSizeValidator implements ConstraintValidator<CustomSize, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return value == null || value.isEmpty() || value.length() >= 7;
    }
}
