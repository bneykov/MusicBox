package musicbox.MusicBox.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import musicbox.MusicBox.repositories.UserRepository;
import musicbox.MusicBox.validation.annotation.UniqueUsername;


public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {
    private final UserRepository userRepository;

    public UniqueUsernameValidator(UserRepository userRepository) {
        this.userRepository = userRepository;

    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return this.userRepository.findByUsername(value).isEmpty();
    }
}
