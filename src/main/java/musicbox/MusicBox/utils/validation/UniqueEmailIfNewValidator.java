package musicbox.MusicBox.utils.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import musicbox.MusicBox.model.entity.UserEntity;
import musicbox.MusicBox.repositories.UserRepository;
import musicbox.MusicBox.services.user.UserService;
import musicbox.MusicBox.utils.validation.annotation.UniqueEmailIfNew;

import java.util.Objects;

public class UniqueEmailIfNewValidator implements ConstraintValidator<UniqueEmailIfNew, String> {
    private final UserRepository userRepository;
    private final UserService userService;

    public UniqueEmailIfNewValidator(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        UserEntity user = this.userRepository.findByEmail(value).orElse(null);
        return user == null || Objects.equals(user.getId(), this.userService.getCurrentUser().getId());
    }
}
