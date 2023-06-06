package musicbox.MusicBox.services.user;

import musicbox.MusicBox.model.dto.UserRegisterDTO;
import musicbox.MusicBox.model.dto.UserUpdateDTO;
import musicbox.MusicBox.model.dto.UserViewDTO;
import musicbox.MusicBox.model.entity.UserEntity;
import musicbox.MusicBox.model.entity.UserRole;
import musicbox.MusicBox.model.enums.RoleEnum;
import musicbox.MusicBox.repositories.RoleRepository;
import musicbox.MusicBox.repositories.UserRepository;
import musicbox.MusicBox.utils.errors.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;

        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;

    }

    public UserEntity getUserById(Long id) {
        return this.userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, "User"));
    }

    public List<UserEntity> getUsers() {
        return this.userRepository.findAll();
    }

    public List<UserViewDTO> mapUsers(List<UserEntity> users) {
        return users.stream().map(user -> this.modelMapper.map(user, UserViewDTO.class)).toList();
    }
    //Return the current user as an instance of CustomUserDetails
    public UserEntity getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails user) {
            return this.userRepository.findById(user.getId())
                    .orElseThrow(() -> new ObjectNotFoundException(user.getId(), "User"));
        }
        return null;
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
        UserRole userRole = this.roleRepository.findUserRoleByRole(RoleEnum.USER)
                .orElseThrow(() -> new ObjectNotFoundException("Role"));
        user.setRoles(List.of(userRole));
        this.userRepository.save(user);
    }

    //Save new user with the data from the DTO
    public void register(UserRegisterDTO registerDTO) {

        UserRole userRole = this.roleRepository.findUserRoleByRole(RoleEnum.USER)
                .orElseThrow(() -> new ObjectNotFoundException("Role"));
        UserEntity user = this.modelMapper.map(registerDTO, UserEntity.class);
        user.setRoles(List.of(userRole));
        user.setPlaylists(new HashSet<>());
        user.setCreated(LocalDateTime.now());
        user.setModified(LocalDateTime.now());
        user.setLastLoggedIn(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        this.userRepository.save(user);

    }

    public void update(UserUpdateDTO updateDTO, Long id) {
        UserEntity user = this.getUserById(id);
        user.setName(updateDTO.getName());
        user.setUsername(updateDTO.getUsername());
        user.setEmail(updateDTO.getEmail());
        if (updateDTO.getImageUrl() != null) {
            user.setImageUrl(updateDTO.getImageUrl());
            user.setImageUUID(updateDTO.getImageUUID());
        }
        if (!updateDTO.getNewPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateDTO.getNewPassword()));
        }
        user.setModified(LocalDateTime.now());
        this.userRepository.save(user);
    }

}
