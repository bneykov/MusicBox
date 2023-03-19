package musicbox.MusicBox.services;

import musicbox.MusicBox.model.entity.UserEntity;
import musicbox.MusicBox.model.entity.UserRole;
import musicbox.MusicBox.model.enums.RoleEnum;
import musicbox.MusicBox.repositories.UserRepository;
import musicbox.MusicBox.services.user.CustomUserDetails;
import musicbox.MusicBox.services.user.UserDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class UserDetailsTest {
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDetailsServiceImpl = new UserDetailsServiceImpl(mockUserRepository);
    }

    @Test
    void testLoadUserByUsernameSuccess() {
        // Arrange
        List<UserRole> roles = new ArrayList<>();
        roles.add(new UserRole(RoleEnum.USER));
        roles.add(new UserRole(RoleEnum.ADMIN));
        UserEntity testUser = UserEntity.builder()
                .id(1L)
                .email("test@abv.bg")
                .username("test")
                .name("Test")
                .password("topsecret")
                .roles(roles)
                .imageUrl("http://test/image.com")
                .imageUUID("testUUID")
                .playlists(new HashSet<>())
                .created(LocalDateTime.of(2023, 1,1, 1, 1))
                .modified(LocalDateTime.of(2023, 1,1, 1, 1))
                .lastLoggedIn(LocalDateTime.of(2023, 1,1, 1, 1))
                .build();
        when(mockUserRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        // Act
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsServiceImpl.loadUserByUsername(testUser.getUsername());

        // Assert
        Assertions.assertEquals(testUser.getId(), userDetails.getId());
        Assertions.assertEquals(testUser.getUsername(), userDetails.getUsername());
        Assertions.assertEquals(testUser.getEmail(), userDetails.getEmail());
        Assertions.assertEquals(testUser.getName(), userDetails.getName());
        Assertions.assertEquals(testUser.getPassword(), userDetails.getPassword());
        Assertions.assertEquals(testUser.getImageUrl(), userDetails.getImageUrl());
        Assertions.assertEquals(testUser.getImageUUID(), userDetails.getImageUUID());
        Assertions.assertEquals(testUser.getModified(), userDetails.getModified());
        Assertions.assertEquals(testUser.getCreated(), userDetails.getCreated());
        Assertions.assertEquals(testUser.getLastLoggedIn(), userDetails.getLastLoggedIn());
        Assertions.assertEquals(testUser.getPlaylists().size(), 0);
        Assertions.assertEquals(testUser.getRoles(), roles);
    }

    @Test
    void testLoadUserByUsernameFailure() {
        // Arrange
        String username = "non_existing_user";
        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userDetailsServiceImpl.loadUserByUsername(username));
    }
}
