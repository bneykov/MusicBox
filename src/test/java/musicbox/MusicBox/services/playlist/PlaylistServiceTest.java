package musicbox.MusicBox.services.playlist;

import musicbox.MusicBox.model.dto.PlaylistDTO;
import musicbox.MusicBox.model.entity.Playlist;
import musicbox.MusicBox.model.entity.Song;
import musicbox.MusicBox.model.entity.UserEntity;
import musicbox.MusicBox.model.enums.Genre;
import musicbox.MusicBox.repositories.PlaylistRepository;
import musicbox.MusicBox.repositories.SongRepository;
import musicbox.MusicBox.repositories.UserRepository;
import musicbox.MusicBox.services.user.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlaylistServiceTest {
    private PlaylistService playlistService;

    @Mock
    private SongRepository songRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private ModelMapper modelMapper;

    Song song = Song.builder()
            .id(1L)
            .name("testSong")
            .imageUrl("testImageUrl")
            .imageUUID("testImageUUID")
            .path("testPath")
            .duration(100)
            .path("testPath")
            .genre(Genre.POP)
            .artists(new HashSet<>())
            .playlists(new HashSet<>())
            .created(LocalDateTime.now())
            .modified(LocalDateTime.now())
            .build();
    Song song2 = Song.builder()
            .id(2L)
            .name("testSong2")
            .imageUrl("testImageUrl2")
            .imageUUID("testImageUUID2")
            .path("testPath2")
            .duration(50)
            .path("testPath2")
            .genre(Genre.ROCK)
            .artists(new HashSet<>())
            .playlists(new HashSet<>())
            .created(LocalDateTime.now())
            .modified(LocalDateTime.now())
            .build();
    UserEntity user = UserEntity.builder()
            .id(1L)
            .name("testUser")
            .imageUrl("testImageUrl2")
            .imageUUID("testImageUUID2")
            .playlists(new HashSet<>())
            .build();
    CustomUserDetails userDetails = CustomUserDetails.builder()
            .id(1L)
            .build();

    Playlist playlist = Playlist.builder()
            .id(1L)
            .name("testPlaylist")
            .imageUrl("testImageUrl")
            .imageUUID("testImageUUID")
            .userEntity(user)
            .songs(new HashSet<>(Set.of(song, song2)))
            .created(LocalDateTime.now())
            .modified(LocalDateTime.now())
            .build();
    Playlist playlist2 = Playlist.builder()
            .id(2L)
            .name("testPlaylist2")
            .imageUrl("testImageUrl2")
            .imageUUID("testImageUUID2")
            .userEntity(user)
            .songs(new HashSet<>(Set.of(song)))
            .created(LocalDateTime.now())
            .modified(LocalDateTime.now())
            .build();

    PlaylistDTO playlistDTO = PlaylistDTO.builder()
            .name("testPlaylist")
            .imageUrl("testImageUrl")
            .imageUUID("testImageUUID")
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        playlistService = new PlaylistService(modelMapper, playlistRepository, userRepository, songRepository);
    }


    @Test
    @DisplayName("getPlaylistById returns Playlist when given valid id")
    void testGetPlaylistByIdWithValidId() {

        when(playlistRepository.findById(playlist.getId())).thenReturn(Optional.of(playlist));
        Playlist actual = playlistService.getPlaylistById(playlist.getId());
        assertEquals(playlist, actual);
    }

    @Test
    @DisplayName("getPlaylistById returns null when given invalid id")
    void testGetPlaylistByIdWithInvalidId() {
        when(playlistRepository.findById(any())).thenReturn(Optional.empty());
        Playlist actual = playlistService.getPlaylistById(playlist.getId());
        assertNull(actual);
    }

    @Test
    @DisplayName("getPlaylistsByUserId returns all playlists of the given user")
    void testGetPlaylistsByUserId() {
        user.setPlaylists(Set.of(playlist, playlist2));
        Set<Playlist> playlists = new HashSet<>(user.getPlaylists());
        when(playlistRepository.findAllByUserEntityId(user.getId())).thenReturn(playlists);
        assertEquals(playlists, playlistService.getUserPlaylists(user.getId()));
    }

    @Test
    @DisplayName("addPlaylist adds playlist to the database")
    void testAddPlaylist(){
        playlist.setUserEntity(null);
        when(modelMapper.map(playlistDTO, Playlist.class)).thenReturn(playlist);
        when(userRepository.findById(userDetails.getId())).thenReturn(Optional.of(user));
        playlistService.addPlaylist(playlistDTO, userDetails);
        verify(playlistRepository, times(1)).save(playlist);
        assertEquals(playlistDTO.getName(), playlist.getName());
        assertEquals(playlistDTO.getImageUUID(), playlist.getImageUUID());
        assertEquals(playlistDTO.getImageUrl(), playlist.getImageUrl());
        assertEquals(user, playlist.getUserEntity());
        assertTrue(playlist.getSongs().isEmpty());
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        assertEquals(currentDateTime, playlist.getCreated());
        assertEquals(currentDateTime, playlist.getModified());
    }
    @Test
    @DisplayName("addSongToPlaylist adds song to the playlist")
    void testAddSongToPlaylist(){
        playlist.setSongs(new HashSet<>(Set.of(song2)));
        int playlistCountBeforeAdd = playlist.getSongs().size();
        when(playlistRepository.findById(playlist.getId())).thenReturn(Optional.of(playlist));
        when(songRepository.findById(song.getId())).thenReturn(Optional.of(song));
        playlistService.addSongToPlaylist(playlist.getId(), song.getId());
        verify(playlistRepository, times(1)).save(playlist);
        assertTrue(playlist.getSongs().contains(song));
        assertEquals(playlistCountBeforeAdd + 1, playlist.getSongs().size());
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        assertEquals(currentDateTime, playlist.getModified());
    }
    @Test
    @DisplayName("removeSongFromPlaylist removes song from the playlist")
    void testRemoveSongFromPlaylist(){
        playlist.setSongs(new HashSet<>(Set.of(song, song2)));
        int playlistCountBeforeRemove= playlist.getSongs().size();
        when(playlistRepository.findById(playlist.getId())).thenReturn(Optional.of(playlist));
        when(songRepository.findById(song.getId())).thenReturn(Optional.of(song));
        playlistService.removeSongFromPlaylist(playlist.getId(), song.getId());
        verify(playlistRepository, times(1)).save(playlist);
        assertFalse(playlist.getSongs().contains(song));
        assertEquals(playlistCountBeforeRemove - 1, playlist.getSongs().size());
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        assertEquals(currentDateTime, playlist.getModified());
    }

    @Test
    @DisplayName("removePlaylist removes playlist from the database")
    void testRemovePlaylist(){
        playlistService.removePlaylist(playlist.getId());
        verify(playlistRepository, times(1)).deleteById(playlist.getId());
    }

}
