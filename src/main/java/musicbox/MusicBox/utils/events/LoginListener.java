package musicbox.MusicBox.utils.events;

import musicbox.MusicBox.model.entity.UserEntity;
import musicbox.MusicBox.repositories.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LoginListener implements ApplicationListener<OnLoginEvent> {
    private final UserRepository userRepository;

    public LoginListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(OnLoginEvent event) {
        UserEntity user = event.getUser();
        user.setLastLoggedIn(LocalDateTime.now());
        userRepository.save(user);
    }
}
