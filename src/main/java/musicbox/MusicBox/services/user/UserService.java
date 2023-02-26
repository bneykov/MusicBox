package musicbox.MusicBox.services.user;

import jakarta.servlet.http.HttpSession;
import musicbox.MusicBox.model.dto.UserLoginDTO;
import musicbox.MusicBox.model.dto.UserRegisterDTO;
import musicbox.MusicBox.model.entity.Artist;
import musicbox.MusicBox.model.entity.User;
import musicbox.MusicBox.repositories.UserRepository;
import musicbox.MusicBox.utils.LoggedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final LoggedUser loggedUser;

    private final HttpSession httpSession;

    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, LoggedUser loggedUser, HttpSession httpSession, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.loggedUser = loggedUser;
        this.httpSession = httpSession;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(UserRegisterDTO userRegisterDTO){
        if (!userRegisterDTO.getPassword().equals(userRegisterDTO.getConfirmPassword())){
            throw new RuntimeException("Passwords don't match");
        }
        User user = User.builder()
                .email(userRegisterDTO.getEmail())
                .name(userRegisterDTO.getName())
                .password(passwordEncoder.encode(userRegisterDTO.getPassword()))
                .created(LocalDateTime.of(LocalDate.now(), LocalTime.now()))
                .modified(LocalDateTime.of(LocalDate.now(), LocalTime.now())).build();
        userRepository.save(user);
    }

    public boolean login(UserLoginDTO loginDTO){
       User user = this.userRepository.findByEmail(loginDTO.getEmail()).orElse(null);
        if (user == null){
            logger.debug("User with name [{}] not found", loginDTO.getEmail());
            return false;
        }
        var rawPassword = loginDTO.getPassword();
        var encodedPassword = user.getPassword();
        boolean success = passwordEncoder.matches(rawPassword, encodedPassword);
        if (success){
            login(user);
        }
        return success;
    }


    private void login(User user){
        this.loggedUser.setName(user.getName());
        this.loggedUser.setEmail(user.getEmail());
        this.loggedUser.setId(user.getId());
    }

    public void logout(){
        this.loggedUser.setEmail(null);
        this.loggedUser.setId(null);
        this.loggedUser.setName(null);
        this.httpSession.invalidate();
    }
}
