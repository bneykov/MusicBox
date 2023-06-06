package musicbox.MusicBox.services.email;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestEmail implements CommandLineRunner {

    private final EmailService emailService;

    public TestEmail(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void run(String... args) {
        emailService.sendAccountTerminationEmail("bobivelik@abv.bg", "bneikov",
                "01.01.2024");
    }
}
