package musicbox.MusicBox.utils.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import musicbox.MusicBox.model.entity.UserEntity;
import musicbox.MusicBox.repositories.UserRepository;
import musicbox.MusicBox.services.user.UserService;
import musicbox.MusicBox.utils.validation.annotation.UniqueUsernameIfNew;

import java.util.Objects;

public class UniqueUsernameIfNewValidator implements ConstraintValidator<UniqueUsernameIfNew, String> {
    private final UserRepository userRepository;
    private final UserService userService;

    public UniqueUsernameIfNewValidator(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        UserEntity user = this.userRepository.findByUsername(value).orElse(null);
        return user == null || Objects.equals(user.getId(), this.userService.getCurrentUser().getId());
    }
}
