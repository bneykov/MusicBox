package musicbox.MusicBox.services;

import musicbox.MusicBox.model.dto.UserLoginDTO;
import musicbox.MusicBox.model.entity.User;
import musicbox.MusicBox.repositories.UserRepository;
import musicbox.MusicBox.user.CurrentUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final CurrentUser currentUser;

    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, CurrentUser currentUser, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.currentUser = currentUser;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean login(UserLoginDTO loginDTO){
        Optional<User> userOptional = userRepository.findByEmail(loginDTO.getEmail());
        if (userOptional.isEmpty()){
            logger.debug("User with name [{}] not found", loginDTO.getEmail());
            return false;
        }
        var rawPassword = loginDTO.getPassword();
        var encodedPassword = userOptional.get().getPassword();
        boolean success = passwordEncoder.matches(rawPassword, encodedPassword);
        if (success){
            login(userOptional.get());
        } else {
            logout();
        }
        return success;
    }


    private void login(User user){
        currentUser.setName(String.join(" ", user.getFirstName(), user.getLastName()));
        currentUser.setLoggedIn(true);
    }

    public void logout(){
        currentUser.clear();
    }
}
