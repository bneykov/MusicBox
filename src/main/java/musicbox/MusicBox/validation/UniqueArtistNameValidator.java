package musicbox.MusicBox.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import musicbox.MusicBox.repositories.ArtistRepository;
import musicbox.MusicBox.repositories.UserRepository;
import musicbox.MusicBox.validation.annotation.UniqueArtistName;
import musicbox.MusicBox.validation.annotation.UniqueUsername;


public class UniqueArtistNameValidator implements ConstraintValidator<UniqueArtistName, String> {
    private final ArtistRepository artistRepository;

    public UniqueArtistNameValidator(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;

    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return this.artistRepository.findByName(value).isEmpty();
    }
}
