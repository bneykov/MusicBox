package musicbox.MusicBox.utils.tasks;

import musicbox.MusicBox.model.entity.UserEntity;
import musicbox.MusicBox.repositories.UserRepository;
import musicbox.MusicBox.services.email.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class SendAccountTerminationEmails {
    private final UserRepository userRepository;
    private final EmailService emailService;

    public SendAccountTerminationEmails(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    //Every day at 00:00 check for users whose account is about to be terminated and send emails.
    @Scheduled(cron = "0 0 0 * * *")
    public void performTask() {

        LocalDateTime notificationDate = LocalDateTime.now().minusMonths(6).plusDays(14);
        List<UserEntity> usersToEmail = this.userRepository.findByLastLoggedInBefore(notificationDate);
        usersToEmail.forEach(user ->
                this.emailService.sendAccountTerminationEmail(user.getEmail(),
                        user.getUsername(), user.getAccountTerminationDate()));

    }
}
