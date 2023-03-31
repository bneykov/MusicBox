package musicbox.MusicBox.services.init;

import jakarta.annotation.PostConstruct;
import musicbox.MusicBox.constants.DefaultImageURLs;
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
    private Artist artist, artist2;
    private Album album, album2;
    private Song song;
    private UserEntity admin, user;
    private Playlist playlist;


    public InitService(RoleRepository roleRepository, UserRepository userRepository, AlbumRepository albumRepository,
                       ArtistRepository artistRepository, PlaylistRepository playlistRepository,
                       SongRepository songRepository, PasswordEncoder passwordEncoder,
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
        user = UserEntity.builder()
                .email("user@abv.bg")
                .username("user")
                .name("Ivan Ivanov")
                .password(passwordEncoder.encode(defaultPassword))
                .roles(List.of(userRole))
                .playlists(new HashSet<>())
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .lastLoggedIn(LocalDateTime.now())
                .build();
        user = userRepository.save(user);
    }

    private void initAdmin() {
        admin = UserEntity.builder()
                .email("admin@abv.bg")
                .username("admin")
                .name("Admin Adminov")
                .password(passwordEncoder.encode(defaultPassword))
                .roles(roleRepository.findAll())
                .playlists(new HashSet<>())
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .lastLoggedIn(LocalDateTime.now())
                .build();
        admin = userRepository.save(admin);
    }

    private void initArtists() {
        if (this.artistRepository.count() == 0) {
            artist = Artist.builder()

                    .name("Selena Gomez")
                    .albums(new HashSet<>())
                    .description("Selena Gomez is an American singer, actress, and producer born on July 22, 1992." +
                            " She began her career on the children's television series \"Barney & Friends\" before rising to fame as the lead actress in Disney Channel's \"Wizards of Waverly Place.\"" +
                            " In 2008, Selena formed the band Selena Gomez & the Scene and released three studio albums with them." +
                            " Her debut solo album, \"Stars Dance,\" released in 2013, topped the charts and solidified her position as a pop icon.")
                    .modified(LocalDateTime.now())
                    .created(LocalDateTime.now())
                    .imageUrl("https://rb.gy/2f1iog")
                    .build();
            artist2 = Artist.builder()
                    .name("Bruno Mars")
                    .albums(new HashSet<>())
                    .description("Bruno Mars is an American singer, songwriter, record producer, and dancer, born on October 8, 1985." +
                            " He began his career as a music producer, writing songs for artists such as Flo Rida and Brandy." +
                            " His debut solo album, \"Doo-Wops & Hooligans,\" released in 2010, made him a household name.")
                    .modified(LocalDateTime.now())
                    .created(LocalDateTime.now())
                    .imageUrl("https://www.eslsongs.com/wp-content/uploads/2022/10/mars.jpg")
                    .build();
            this.artistRepository.saveAll(List.of(artist, artist2));
        }
    }

    private void initAlbums() {
        if (this.albumRepository.count() == 0) {
            album = Album.builder()
                    .name("For You")
                    .artists(Set.of(artist))
                    .created(LocalDateTime.now())
                    .modified(LocalDateTime.now())
                    .imageUrl("https://m.media-amazon.com/images/I/71maiXUoH2L._SX425_.jpg")
                    .build();
            album2 = Album.builder()
                    .name("Shared album")
                    .artists(Set.of(artist, artist2))
                    .created(LocalDateTime.now())
                    .modified(LocalDateTime.now())
                    .imageUrl("https://upload.wikimedia.org/wikipedia/en/5/54/Public_image_ltd_album_cover.jpg")
                    .build();
            this.albumRepository.saveAll(List.of(album, album2));
        }
    }

    private void initSongs() {
        if (this.songRepository.count() == 0) {

            song = Song.builder()

                    .name("Who says")
                    .imageUrl("https://i.scdn.co/image/ab67616d0000b273fddfbee3aafd7fadab0e5460")
                    .artists(Set.of(artist))
                    .album(album)
                    .genre(Genre.POP)
                    .duration(226)
                    .playlists(new HashSet<>())
                    .created(LocalDateTime.now())
                    .modified(LocalDateTime.now())
                    .build();
            song = this.songRepository.save(song);
        }
    }

    private void initPlaylists() {
        if (this.playlistRepository.count() == 0) {

            playlist = Playlist.builder()
                    .songs(Set.of(song))
                    .created(LocalDateTime.now())
                    .modified(LocalDateTime.now())
                    .name("My Playlist")
                    .userEntity(admin)
                    .imageUrl(DefaultImageURLs.DEFAULT_PLAYLIST_IMAGE_URL)
                    .build();
            playlist.setSongs(Set.of(song));
            this.playlistRepository.save(playlist);
        }

    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Artist getArtist2() {
        return artist2;
    }

    public void setArtist2(Artist artist2) {
        this.artist2 = artist2;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Album getAlbum2() {
        return album2;
    }

    public void setAlbum2(Album album2) {
        this.album2 = album2;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public UserEntity getAdmin() {
        return admin;
    }

    public void setAdmin(UserEntity admin) {
        this.admin = admin;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public Set<Artist> getArtists() {
        return new HashSet<>(Set.of(artist, artist2));
    }

    public Set<Album> getAlbums() {
        return new HashSet<>(Set.of(album, album2));
    }

    public Set<Song> getSongs() {
        return new HashSet<>(Set.of(song));
    }

    public Set<UserEntity> getUsers() {
        return new HashSet<>(Set.of(admin, user));
    }

    public Set<Playlist> getPlaylists() {
        return new HashSet<>(Set.of(playlist));
    }

    public void clearDatabase() {
        playlistRepository.deleteAll();
        artistRepository.deleteAll();
        songRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        albumRepository.deleteAll();
    }
}
