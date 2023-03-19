package musicbox.MusicBox.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import musicbox.MusicBox.validation.annotation.ImageNotEmpty;
import org.springframework.web.multipart.MultipartFile;

public class ImageNotEmptyValidator implements ConstraintValidator<ImageNotEmpty, MultipartFile> {


    @Override
    public boolean isValid(MultipartFile image, ConstraintValidatorContext constraintValidatorContext) {
        return !image.isEmpty();
    }
}
