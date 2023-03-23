package musicbox.MusicBox.services.song;

import musicbox.MusicBox.model.dto.SongDTO;
import musicbox.MusicBox.model.entity.Album;
import musicbox.MusicBox.model.entity.Artist;
import musicbox.MusicBox.model.entity.Playlist;
import musicbox.MusicBox.model.entity.Song;
import musicbox.MusicBox.model.enums.Genre;
import musicbox.MusicBox.repositories.AlbumRepository;
import musicbox.MusicBox.repositories.SongRepository;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SongServiceTest {
    private SongService songService;
    @Mock
    private SongRepository songRepository;
    @Mock
    private AlbumRepository albumRepository;
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
    Album album = Album.builder()
            .id(1L)
            .name("testAlbum")
            .songs(new HashSet<>(Set.of(song, song2)))
            .build();
    Album album2 = Album.builder()
            .id(2L)
            .name("testAlbum2")
            .songs(new HashSet<>(Set.of(song, song2)))
            .build();

    Playlist playlist = Playlist.builder()
            .id(1L)
            .name("testPlaylist")
            .build();
    Playlist playlist2 = Playlist.builder()
            .id(2L)
            .name("testPlaylist2")
            .build();

    Artist artist = Artist.builder()
            .id(1L)
            .name("testArtist")
            .songs(new HashSet<>(Set.of(song, song2)))
            .build();
    Artist artist2 = Artist.builder()
            .id(2L)
            .name("testArtist2")
            .songs(new HashSet<>(Set.of(song, song2)))
            .build();

    SongDTO songDTO = SongDTO.builder()
            .name("testSong2")
            .imageUrl("testImageUrl2")
            .imageUUID("testImageUUID2")
            .path("testPath2")
            .duration(50)
            .genre(Genre.ROCK)
            .album(album2.getId())
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        songService = new SongService(songRepository,
                modelMapper, albumRepository);

    }

    @Test
    @DisplayName("getSongs returns all songs")
    void testGetSongs() {
        List<Song> songs = new ArrayList<>();
        songs.add(song);
        songs.add(song2);
        when(songRepository.findAll()).thenReturn(songs);
        assertEquals(songs, songService.getSongs());
    }

    @Test
    @DisplayName("getHomeSongs returns limited number of songs")
    void getHomeSongsReturnsLimitedSongs() {
        List<Song> songs = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            songs.add(new Song());
        }
        when(songRepository.findAll()).thenReturn(songs);
        List<Song> result = songService.getHomeSongs();
        assertEquals(6, result.size());
        songs.remove(6);
        assertTrue(result.containsAll(songs));
    }

    @Test
    @DisplayName("getSongById returns Song when given valid id")
    void testGetSongByIdWithValidId() {

        when(songRepository.findById(song.getId())).thenReturn(Optional.of(song));
        Song actual = songService.getSongById(song.getId());
        assertEquals(song, actual);
    }

    @Test
    @DisplayName("getSongById returns null when given invalid id")
    void testGetSongByIdWithInvalidId() {
        Song actual = songService.getSongById(1L);
        assertNull(actual);
    }

    @Test
    @DisplayName("addSong saves new song to the database")
    void testAddSongWithValidData() {

        album2.setArtists(new HashSet<>(Set.of(artist, artist2)));
        song2.setAlbum(album2);

        when(modelMapper.map(songDTO, Song.class)).thenReturn(song2);
        when(albumRepository.findById(songDTO.getAlbum()))
                .thenReturn(Optional.of(album2));

        songService.addSong(songDTO);

        verify(songRepository, times(1)).save(song2);
        assertEquals(songDTO.getName(), song2.getName());
        assertEquals(songDTO.getImageUUID(), song2.getImageUUID());
        assertEquals(songDTO.getImageUrl(), song2.getImageUrl());
        assertEquals(songDTO.getPath(), song2.getPath());
        assertEquals(songDTO.getGenre(), song2.getGenre());
        assertEquals(songDTO.getDuration(), song2.getDuration());
        assertEquals(songDTO.getAlbum(), song2.getAlbum().getId());
        assertEquals(Set.of(artist, artist2), song2.getArtists());
        assertTrue(song2.getPlaylists().isEmpty());
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        assertEquals(currentDateTime, song2.getCreated());
        assertEquals(currentDateTime, song2.getModified());

    }
@Test
    @DisplayName("getSongWithoutRelations returns Song without relations")
    void testRemoveSongConnections(){
        when(songRepository.findById(song.getId())).thenReturn(Optional.of(song));
        songService.removeSongConnections(song.getId());
        verify(songRepository, times(1)).save(song);
        assertTrue(song.getPlaylists().isEmpty());
        assertTrue(song.getArtists().isEmpty());
        assertNull(song.getAlbum());

    }

}
