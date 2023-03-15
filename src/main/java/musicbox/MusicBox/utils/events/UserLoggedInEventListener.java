package musicbox.MusicBox.utils.events;

import musicbox.MusicBox.model.entity.UserEntity;
import musicbox.MusicBox.repositories.UserRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserLoggedInEventListener {
    private final UserRepository userRepository;

    public UserLoggedInEventListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @EventListener
    public void handleUserLoggedInEvent(UserLoggedInEvent event) {
        UserEntity user = event.getUser();
        user.setLastLoggedIn(LocalDateTime.now());
        userRepository.save(user);
    }
}
