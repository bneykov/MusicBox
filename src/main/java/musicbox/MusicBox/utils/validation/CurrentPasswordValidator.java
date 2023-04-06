package musicbox.MusicBox.utils.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import musicbox.MusicBox.services.user.UserService;
import musicbox.MusicBox.utils.validation.annotation.ValidCurrentPassword;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CurrentPasswordValidator implements ConstraintValidator<ValidCurrentPassword, String> {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public CurrentPasswordValidator(UserService userService, PasswordEncoder passwordEncoder) {

        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return passwordEncoder.matches(value, this.userService.getCurrentUser().getPassword());
    }
}
