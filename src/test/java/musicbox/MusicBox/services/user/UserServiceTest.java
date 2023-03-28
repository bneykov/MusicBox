package musicbox.MusicBox.services.user;

import musicbox.MusicBox.model.dto.UserRegisterDTO;
import musicbox.MusicBox.model.entity.UserEntity;
import musicbox.MusicBox.model.entity.UserRole;
import musicbox.MusicBox.model.enums.RoleEnum;
import musicbox.MusicBox.repositories.RoleRepository;
import musicbox.MusicBox.repositories.UserRepository;
import musicbox.MusicBox.utils.errors.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;

    @Mock
    private RoleRepository roleRepository;


    @Captor
    private ArgumentCaptor<UserEntity> userArgumentCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, passwordEncoder, roleRepository, modelMapper);
    }

    @Test
    @DisplayName("getUsers returns all users")
    void testGetUsers() {
        List<UserEntity> users = new ArrayList<>();
        users.add(new UserEntity());
        users.add(new UserEntity());
        when(userRepository.findAll()).thenReturn(users);
        assertEquals(users, userService.getUsers());
    }

    @Test
    @DisplayName("getUserById returns User when given valid id")
    void testGetUserByIdWithValidId() {
        UserEntity user = UserEntity.builder()
                .id(1L)
                .build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        UserEntity actual = userService.getUserById(user.getId());
        assertEquals(user, actual);
    }

    @Test
    @DisplayName("getUserById throws ObjectNotFound when given invalid id")
    void testGetUserByIdWithInvalidId() {
        when(userRepository.findById(5L)).thenReturn(Optional.empty());
        try {
            userService.getUserById(5L);
            fail("Expected  ObjectNotFoundException");
        } catch (ObjectNotFoundException exception) {
            assertEquals("Object with ID 5 of type User not found", exception.getMessage());
        }
    }

    @Test
    @DisplayName("register adds user to the database")
    void testRegister() {
        UserRegisterDTO userRegisterDTO = UserRegisterDTO.builder()
                .username("testUsername")
                .email("test@test.com")
                .name("testName")
                .password("topsecret")
                .imageUrl("testImageUrl")
                .build();
        UserEntity user = UserEntity.builder()
                .username("testUsername")
                .email("test@test.com")
                .name("testName")
                .password("topsecret")
                .imageUrl("testImageUrl")
                .build();

        UserRole userRole = new UserRole(RoleEnum.USER);
        when(modelMapper.map(userRegisterDTO, UserEntity.class)).thenReturn(user);
        when(roleRepository.findUserRoleByRole(RoleEnum.USER)).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode(userRegisterDTO.getPassword())).thenReturn("encodedPassword");
        userService.register(userRegisterDTO);
        verify(userRepository).save(userArgumentCaptor.capture());
        UserEntity savedUser = userArgumentCaptor.getValue();
        assertEquals(userRegisterDTO.getUsername(), savedUser.getUsername());
        assertEquals(userRegisterDTO.getEmail(), savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals(userRegisterDTO.getName(), savedUser.getName());
        assertEquals(userRegisterDTO.getImageUrl(), savedUser.getImageUrl());
        assertEquals(List.of(userRole), savedUser.getRoles());
        assertTrue(savedUser.getPlaylists().isEmpty());
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        assertEquals(currentDateTime, savedUser.getCreated());
        assertEquals(currentDateTime, savedUser.getModified());
        assertEquals(currentDateTime, savedUser.getLastLoggedIn());
    }

    @Test
    @DisplayName("changeRole adds admin role to non-admins")
    void testChangeRoleWhenUserNotAdmin(){
        UserRole userRole = UserRole.builder()
                .role(RoleEnum.USER)
                .build();
        UserRole adminRole = UserRole.builder()
                .role(RoleEnum.ADMIN)
                .build();
        UserEntity user = UserEntity.builder()
                .username("testUsername")
                .email("test@test.com")
                .name("testName")
                .password("topsecret")
                .imageUrl("testImageUrl")
                .roles(new ArrayList<>(List.of(userRole)))
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(roleRepository.findAll()).thenReturn(List.of(userRole,adminRole));
        userService.changeRole(user.getId());
        verify(userRepository).save(userArgumentCaptor.capture());
        UserEntity savedUser = userArgumentCaptor.getValue();
        assertEquals(List.of(userRole, adminRole), savedUser.getRoles());

    }
    @Test
    @DisplayName("changeRole removes admin role from admins")
    void testChangeRoleWhenUserIsAdmin(){
        UserRole userRole = UserRole.builder()
                .role(RoleEnum.USER)
                .build();
        UserRole adminRole = UserRole.builder()
                .role(RoleEnum.ADMIN)
                .build();
        UserEntity user = UserEntity.builder()
                .username("testUsername")
                .email("test@test.com")
                .name("testName")
                .password("topsecret")
                .imageUrl("testImageUrl")
                .roles(new ArrayList<>(List.of(userRole, adminRole)))
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(roleRepository.findUserRoleByRole(RoleEnum.USER)).thenReturn(Optional.of(userRole));
        userService.changeRole(user.getId());
        verify(userRepository).save(userArgumentCaptor.capture());
        UserEntity savedUser = userArgumentCaptor.getValue();
        assertEquals(List.of(userRole), savedUser.getRoles());

    }


}
