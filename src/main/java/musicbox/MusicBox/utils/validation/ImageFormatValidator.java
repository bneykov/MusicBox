package musicbox.MusicBox.utils.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import musicbox.MusicBox.services.cloudinary.CloudinaryService;
import musicbox.MusicBox.utils.validation.annotation.ValidImageFormat;
import org.springframework.web.multipart.MultipartFile;

public class ImageFormatValidator implements ConstraintValidator<ValidImageFormat, MultipartFile> {
    private final CloudinaryService cloudinaryService;

    public ImageFormatValidator(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public boolean isValid(MultipartFile image, ConstraintValidatorContext constraintValidatorContext) {
        //If there is no selected file
        if (image == null || image.isEmpty()) {
            return true;
        }
        return this.cloudinaryService.isImage(image);
    }
}
