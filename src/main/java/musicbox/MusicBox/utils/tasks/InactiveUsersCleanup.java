package musicbox.MusicBox.utils.tasks;

import musicbox.MusicBox.model.entity.UserEntity;
import musicbox.MusicBox.repositories.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class InactiveUsersCleanup {
    private final UserRepository userRepository;

    public InactiveUsersCleanup(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void performTask() {

        LocalDateTime dateSixMonthsAgo = LocalDateTime.now().minusMonths(6);
        List<UserEntity> usersToDelete = this.userRepository.findByLastLoggedInBefore(dateSixMonthsAgo);
        usersToDelete.forEach(user -> {
            user.getRoles().clear();
            this.userRepository.save(user);
        });
        this.userRepository.deleteAll(usersToDelete);
    }
}
