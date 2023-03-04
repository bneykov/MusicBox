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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;

        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    public void register(UserRegisterDTO registerDTO) {

        UserRole userRole = this.roleRepository.findUserRoleByRole(RoleEnum.USER).orElseThrow();
        UserEntity user =this.modelMapper.map(registerDTO, UserEntity.class);
        user.setRoles(List.of(userRole));
        user.setCreated(LocalDateTime.now());
        user.setModified(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        this.userRepository.save(user);
    }

}
