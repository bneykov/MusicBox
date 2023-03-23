package musicbox.MusicBox.utils.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import musicbox.MusicBox.model.entity.UserEntity;
import musicbox.MusicBox.repositories.PlaylistRepository;
import musicbox.MusicBox.services.user.UserService;
import musicbox.MusicBox.utils.validation.annotation.UniquePlaylistName;


public class UniquePlaylistNameValidator implements ConstraintValidator<UniquePlaylistName, String> {
    private final UserService userService;
    private final PlaylistRepository playlistRepository;

    public UniquePlaylistNameValidator(UserService userService, PlaylistRepository playlistRepository) {
        this.userService = userService;
        this.playlistRepository = playlistRepository;
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        UserEntity owner = userService.getCurrentUser();
        return playlistRepository.findAllByUserEntityId(owner.getId())
                .stream()
                .noneMatch(playlist -> playlist.getName().equals(value));
    }
}
