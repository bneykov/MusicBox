package musicbox.MusicBox.services.user;

import musicbox.MusicBox.model.dto.UserRegisterDTO;
import musicbox.MusicBox.model.entity.UserEntity;
import musicbox.MusicBox.model.entity.UserRole;
import musicbox.MusicBox.model.enums.RoleEnum;
import musicbox.MusicBox.repositories.RoleRepository;
import musicbox.MusicBox.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final ModelMapper modelMapper;
    private final UserDetailsService userDetailsService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, ModelMapper modelMapper, UserDetailsService userDetailsService, SecurityContextRepository securityContextRepository) {
        this.userRepository = userRepository;

        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.userDetailsService = userDetailsService;
    }

    public UserEntity getUserById(Long id) {
        return this.userRepository.findById(id).orElseThrow();
    }

    public UserEntity findByUsername(String username) {
        return this.userRepository.findByUsername(username).orElseThrow();
    }

    public List<UserEntity> getUsers() {
        return this.userRepository.findAll();
    }

    public void changeRole(Long id) {
        UserEntity user = this.getUserById(id);
        user.setModified(LocalDateTime.now());
        if (user.getRoles().size() > 1) {
            makeUser(user);
        } else {
            makeAdmin(user);
        }


    }

    private void makeAdmin(UserEntity user) {
        user.setRoles(this.roleRepository.findAll());
        this.userRepository.save(user);
    }

    private void makeUser(UserEntity user) {
        UserRole userRole = this.roleRepository.findUserRoleByRole(RoleEnum.USER).orElse(null);
        user.setRoles(List.of(userRole));
        this.userRepository.save(user);
    }


    public void register(UserRegisterDTO registerDTO, Consumer<Authentication> successfulRegisterProcessor) {

        UserRole userRole = this.roleRepository.findUserRoleByRole(RoleEnum.USER).orElseThrow();
        UserEntity user = this.modelMapper.map(registerDTO, UserEntity.class);
        user.setRoles(List.of(userRole));
        user.setCreated(LocalDateTime.now());
        user.setModified(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        this.userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(registerDTO.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                userDetails.getPassword(), userDetails.getAuthorities());

        successfulRegisterProcessor.accept(authentication);


    }

}
