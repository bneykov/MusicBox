package musicbox.MusicBox.services.album;

import musicbox.MusicBox.model.dto.AlbumDTO;
import musicbox.MusicBox.model.entity.Album;
import musicbox.MusicBox.model.entity.Artist;
import musicbox.MusicBox.model.entity.Playlist;
import musicbox.MusicBox.model.entity.Song;
import musicbox.MusicBox.repositories.AlbumRepository;
import musicbox.MusicBox.repositories.ArtistRepository;
import musicbox.MusicBox.repositories.SongRepository;
import musicbox.MusicBox.utils.errors.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlbumServiceTest {

    private AlbumService albumService;
    @Mock
    private AlbumRepository mockAlbumRepository;
    @Mock
    private SongRepository mockSongRepository;

    @Mock
    private ArtistRepository mockArtistRepository;

    @Mock
    private ModelMapper mockModelMapper;


    @Captor
    private ArgumentCaptor<Album> albumArgumentCaptor;
    Song song = Song.builder()
            .id(1L)
            .name("testSong")
            .build();
    Song song2 = Song.builder()
            .id(2L)
            .name("testSong2")
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

    AlbumDTO albumDTO = AlbumDTO.builder()
            .name("Test")
            .imageUUID("testUUID")
            .imageUrl("testUrl")
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        albumService =
                new AlbumService(
                        mockAlbumRepository,
                        mockModelMapper,
                        mockSongRepository,
                        mockArtistRepository);

    }


    @Test
    @DisplayName("addAlbum saves new album to the database")
    void testAddAlbumWithValidData() {
        Set<Long> artistIds= Set.of(1L, 2L);
        albumDTO.setArtists(new HashSet<>(artistIds));
        Album album = Album.builder()
                .name("Test")
                .imageUUID("testUUID")
                .imageUrl("testUrl")
                .build();
        when(mockModelMapper.map(albumDTO, Album.class)).thenReturn(album);
        when(mockArtistRepository.findAllById(artistIds))
                .thenReturn(new ArrayList<>(List.of(artist, artist2)));


        albumService.addAlbum(albumDTO);

        verify(mockAlbumRepository).save(albumArgumentCaptor.capture());
        Album savedAlbum = albumArgumentCaptor.getValue();
        assertEquals(albumDTO.getName(), savedAlbum.getName());
        assertEquals(albumDTO.getImageUUID(), savedAlbum.getImageUUID());
        assertEquals(albumDTO.getImageUrl(), savedAlbum.getImageUrl());
        assertEquals(Set.of(artist, artist2), savedAlbum.getArtists());
        assertTrue(savedAlbum.getSongs().isEmpty());
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        assertEquals(currentDateTime, savedAlbum.getCreated());
        assertEquals(currentDateTime, savedAlbum.getModified());

    }

    @Test
    @DisplayName("removeSongsFromLinkedEntities removes songs' connections and deletes them")

    public void testRemoveSongsFromLinkedEntities() {

        song.setPlaylists(new HashSet<>(Set.of(playlist)));
        song2.setPlaylists(new HashSet<>(Set.of(playlist)));
        playlist.setSongs(new HashSet<>(Set.of(song, song2)));
        song.setArtists(new HashSet<>(Set.of(artist)));
        song2.setArtists(new HashSet<>(Set.of(artist2)));
        song.setAlbum(album);
        song2.setAlbum(album2);
        albumService.removeSongsFromLinkedEntities(album.getSongs());
        verify(mockSongRepository, times(1)).deleteAll(any());
        assertTrue(song.getPlaylists().isEmpty());
        assertTrue(song.getArtists().isEmpty());
        assertNull(song.getAlbum());
    }


    @Test
    @DisplayName("getAlbums returns all albums")
    void testGetAlbums() {
        List<Album> albums = new ArrayList<>(List.of(album, album2));
        when(mockAlbumRepository.findAll()).thenReturn(albums);
        assertEquals(albums, albumService.getAlbums());
    }

    @Test
    @DisplayName("removeAlbum removes given album")
    void testRemoveAlbum() {
        album.setArtists(new HashSet<>(Set.of(artist, artist2)));
        artist.setAlbums(new HashSet<>(Set.of(album, album2)));
        artist2.setAlbums(new HashSet<>(Set.of(album, album2)));
        song.setArtists(new HashSet<>(album.getArtists()));
        song2.setArtists(new HashSet<>(album.getArtists()));
        song.setPlaylists(new HashSet<>(Set.of(playlist)));
        song2.setPlaylists(new HashSet<>(Set.of(playlist)));
        playlist.setSongs(new HashSet<>(Set.of(song, song2)));
        when(mockAlbumRepository.findById(1L)).thenReturn(Optional.of(album));
        albumService.removeAlbum(album.getId());
        verify(mockAlbumRepository, times(1)).delete(album);

    }

    @Test
    @DisplayName("removeAllAlbumsFromArtist removes all albums of an artist")
    void testRemoveAllAlbumsFromArtist() {
        album.setArtists(new HashSet<>(Set.of(artist, artist2)));
        album2.setArtists(new HashSet<>(Set.of(artist)));
        artist.setAlbums(new HashSet<>(Set.of(album, album2)));
        artist2.setAlbums(new HashSet<>(Set.of(album)));
        song.setArtists(new HashSet<>(album.getArtists()));
        song2.setArtists(new HashSet<>(album.getArtists()));
        song.setPlaylists(new HashSet<>(Set.of(playlist)));
        song2.setPlaylists(new HashSet<>(Set.of(playlist)));
        playlist.setSongs(new HashSet<>(Set.of(song, song2)));
        List<Album> albums = new ArrayList<>(List.of(album));
        List<Artist> artists = new ArrayList<>(List.of(artist, artist2));
        when(mockAlbumRepository.findAllById(any())).thenReturn(albums);
        albumService.removeAllAlbumsFromArtist(artist2.getAlbums());
        verify(mockAlbumRepository, times(1)).deleteAll(albums);
        verify(mockArtistRepository, times(1)).saveAll(new HashSet<>(artists));
        assertTrue(artists.get(1).getAlbums().isEmpty());
        assertEquals(1, artists.get(0).getAlbums().size());
    }


    @Test
    @DisplayName("getSongsByAlbumId returns songs for a specific album")
    void testGetSongsByAlbumId() {
        Set<Song> expected = Set.of(song);
        when(mockSongRepository.findAllByAlbumId(album.getId())).thenReturn(expected);
        Set<Song> actual = albumService.getSongsByAlbumId(1L);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getAlbumById returns correct Album when given valid id")
    void testGetAlbumByIdWithValidId() {
        when(mockAlbumRepository.findById(1L)).thenReturn(Optional.of(album));
        Album actual = albumService.getAlbumById(1L);
        assertEquals(album, actual);
    }
    @Test
    @DisplayName("getAlbumById throws ObjectNotFound when given invalid id")
    void testGetAlbumByIdWithInvalidId() {
        when(mockAlbumRepository.findById(5L)).thenReturn(Optional.empty());
        try {
            albumService.getAlbumById(5L);
            fail("Expected  ObjectNotFoundException");
        } catch (ObjectNotFoundException exception) {
            assertEquals("Object with ID 5 of type Album not found", exception.getMessage());
        }
    }

    @Test
    @DisplayName("getHomeAlbums returns limited number of albums")
    void getHomeAlbumsReturnsLimitedAlbums() {
        List<Album> albums = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            albums.add(new Album());
        }
        when(mockAlbumRepository.findAll()).thenReturn(albums);
        List<Album> result = albumService.getHomeAlbums();
        assertEquals(6, result.size());
    }
}