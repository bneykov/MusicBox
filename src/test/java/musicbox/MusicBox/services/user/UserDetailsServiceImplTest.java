package musicbox.MusicBox.services.user;

import musicbox.MusicBox.model.entity.UserEntity;
import musicbox.MusicBox.model.entity.UserRole;
import musicbox.MusicBox.model.enums.RoleEnum;
import musicbox.MusicBox.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDetailsServiceImpl = new UserDetailsServiceImpl(mockUserRepository);
    }

    @Test
    @DisplayName("loadUserByUsername returns the user with the given username")
    void testLoadUserByUsernameSuccess() {

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
                .created(LocalDateTime.of(2023, 1, 1, 1, 1))
                .modified(LocalDateTime.of(2023, 1, 1, 1, 1))
                .lastLoggedIn(LocalDateTime.of(2023, 1, 1, 1, 1))
                .build();
        when(mockUserRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsServiceImpl.loadUserByUsername(testUser.getUsername());

        assertEquals(testUser.getId(), userDetails.getId());
        assertEquals(testUser.getUsername(), userDetails.getUsername());
        assertEquals(testUser.getEmail(), userDetails.getEmail());
        assertEquals(testUser.getName(), userDetails.getName());
        assertEquals(testUser.getPassword(), userDetails.getPassword());
        assertEquals(testUser.getImageUrl(), userDetails.getImageUrl());
        assertEquals(testUser.getImageUUID(), userDetails.getImageUUID());
        assertEquals(testUser.getModified(), userDetails.getModified());
        assertEquals(testUser.getCreated(), userDetails.getCreated());
        assertEquals(testUser.getLastLoggedIn(), userDetails.getLastLoggedIn());
        assertEquals(testUser.getPlaylists().size(), 0);
        assertEquals(testUser.getRoles(), roles);
    }

    @Test
    @DisplayName("loadUserByUsername throws error if username doesn't exist")
    void testLoadUserByUsernameFailure() {

        String username = "non_existing_user";
        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsServiceImpl.loadUserByUsername(username));
    }
}
