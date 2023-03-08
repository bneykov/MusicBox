package musicbox.MusicBox.services.init;

import jakarta.annotation.PostConstruct;
import musicbox.MusicBox.model.entity.*;
import musicbox.MusicBox.model.enums.Genre;
import musicbox.MusicBox.model.enums.RoleEnum;
import musicbox.MusicBox.repositories.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class InitService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final PlaylistRepository playlistRepository;

    private final SongRepository songRepository;
    private final PasswordEncoder passwordEncoder;
    private final String defaultPassword;

    public InitService(RoleRepository roleRepository, UserRepository userRepository, AlbumRepository albumRepository, ArtistRepository artistRepository, PlaylistRepository playlistRepository, SongRepository songRepository, PasswordEncoder passwordEncoder,
                       @Value("${app.default.password}") String defaultPassword) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
        this.playlistRepository = playlistRepository;
        this.songRepository = songRepository;
        this.passwordEncoder = passwordEncoder;
        this.defaultPassword = defaultPassword;
    }

    @PostConstruct
    public void init() {
        initRoles();
        initUsers();
        initArtists();
        initAlbums();
        initSongs();
        initPlaylists();
    }

    private void initRoles() {
        if (roleRepository.count() == 0) {
            UserRole adminRole = UserRole.builder()
                    .role(RoleEnum.ADMIN)
                    .created(LocalDateTime.now())
                    .modified(LocalDateTime.now())
                    .build();
            UserRole userRole = UserRole.builder()
                    .role(RoleEnum.USER)
                    .created(LocalDateTime.now())
                    .modified(LocalDateTime.now())
                    .build();

            this.roleRepository.save(adminRole);
            this.roleRepository.save(userRole);
        }
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            initAdmin();
            initUser();
        }
    }

    private void initUser() {
        UserRole userRole = roleRepository.findUserRoleByRole(RoleEnum.USER).orElseThrow();
        UserEntity user = UserEntity.builder()
                .email("user@abv.bg")
                .username("user")
                .name("Ivan Ivanov")
                .password(passwordEncoder.encode(defaultPassword))
                .roles(List.of(userRole))
                .playlists(new HashSet<>())
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .build();
        userRepository.save(user);
    }

    private void initAdmin() {
        UserEntity admin = UserEntity.builder()
                .email("admin@abv.bg")
                .username("admin")
                .name("Admin Adminov")
                .password(passwordEncoder.encode(defaultPassword))
                .roles(roleRepository.findAll())
                .playlists(new HashSet<>())
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .build();
        userRepository.save(admin);
    }

    private void initArtists(){
        if (this.artistRepository.count() == 0) {
            Artist artist = Artist.builder()
                    .id(1L)
                    .name("Selena Gomez")
                    .albums(new HashSet<>())
                    .modified(LocalDateTime.now())
                    .created(LocalDateTime.now())
                    .imageUrl("https://rb.gy/2f1iog")
                    .build();
            this.artistRepository.save(artist);
        }
    }
    private void initAlbums(){
        if (this.albumRepository.count() == 0) {

            Artist artist = this.artistRepository.findById(1L).orElseThrow();
            Album album = Album.builder()
                    .id(1L)
                    .name("For You")
                    .artists(Set.of(artist))
                    .created(LocalDateTime.now())
                    .modified(LocalDateTime.now())
                    .cover("https://m.media-amazon.com/images/I/71maiXUoH2L._SX425_.jpg")
                    .build();
            this.albumRepository.save(album);
        }
    }

    private void initSongs(){
        if (this.songRepository.count() == 0) {
            Artist artist = this.artistRepository.findById(1L).orElseThrow();
            Album album = this.albumRepository.findById(1L).orElseThrow();
            Song song = Song.builder()
                    .id(1L)
                    .title("Who says")
                    .imageUrl("https://i.scdn.co/image/ab67616d0000b273fddfbee3aafd7fadab0e5460")
                    .artists(Set.of(artist))
                    .album(album)
                    .genre(Genre.POP)
                    .length(226)
                    .playlists(new HashSet<>())
                    .created(LocalDateTime.now())
                    .modified(LocalDateTime.now())
                    .build();
            this.songRepository.save(song);
        }
    }
    private void initPlaylists(){
        if (this.playlistRepository.count() == 0) {
            UserEntity user = this.userRepository.findByUsername("admin").orElseThrow();
           Song song = this.songRepository.findById(1L).orElseThrow();
           Playlist playlist = Playlist.builder()
                   .songs(Set.of(song))
                   .created(LocalDateTime.now())
                   .modified(LocalDateTime.now())
                   .name("Test Playlist")
                   .userEntity(user)
                   .imageUrl("https://i.pinimg.com/originals/19/f7/68/19f76854a898d32735a5cfdc2d2fc262.jpg")
                   .build();
           playlist.setSongs(Set.of(song));
           this.playlistRepository.save(playlist);
        }
    }

}
