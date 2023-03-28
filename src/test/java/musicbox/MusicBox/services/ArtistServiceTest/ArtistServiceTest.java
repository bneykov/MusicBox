package musicbox.MusicBox.services.ArtistServiceTest;

import musicbox.MusicBox.model.dto.ArtistDTO;
import musicbox.MusicBox.model.entity.Album;
import musicbox.MusicBox.model.entity.Artist;
import musicbox.MusicBox.model.entity.Song;
import musicbox.MusicBox.repositories.AlbumRepository;
import musicbox.MusicBox.repositories.ArtistRepository;
import musicbox.MusicBox.services.album.AlbumService;
import musicbox.MusicBox.services.artist.ArtistService;
import musicbox.MusicBox.utils.errors.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArtistServiceTest {
    @InjectMocks
    private ArtistService artistService;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private AlbumService albumService;

    @Captor
    private ArgumentCaptor<Artist> artistArgumentCaptor;

    Artist artist = Artist.builder()
            .id(1L)
            .name("testArtist")
            .imageUUID("testUUID")
            .imageUrl("testUrl")
            .albums(new HashSet<>())
            .songs(new HashSet<>())
            .created(LocalDateTime.now())
            .modified(LocalDateTime.now())
            .build();

    ArtistDTO artistDTO = ArtistDTO.builder()
            .name("testArtist")
            .imageUUID("testUUID")
            .imageUrl("testUrl")
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        artistService = new ArtistService(artistRepository,
                modelMapper, albumRepository, albumService);

    }

    @Test
    @DisplayName("getArtists returns all artists")
    void testGetArtists() {
        List<Artist> artists = new ArrayList<>();
        artists.add(artist);
        artists.add(new Artist());
        when(artistRepository.findAll()).thenReturn(artists);
        assertEquals(artists, artistService.getArtists());
    }

    @Test
    @DisplayName("getHomeArtists returns limited number of artists")
    void getHomeArtistsReturnsLimitedArtists() {
        List<Artist> artists = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            artists.add(new Artist());
        }
        when(artistRepository.findAll()).thenReturn(artists);
        List<Artist> result = artistService.getHomeArtists();
        assertEquals(6, result.size());
        artists.remove(6);
        assertTrue(result.containsAll(artists));
    }

    @Test
    @DisplayName("getArtistById returns Artist when given valid id")
    void testGetArtistByIdWithValidId() {

        when(artistRepository.findById(artist.getId())).thenReturn(Optional.of(artist));
        Artist actual = artistService.getArtistById(artist.getId());
        assertEquals(artist, actual);
    }

    @Test
    @DisplayName("getArtistById throws ObjectNotFound when given invalid id")
    void testGetArtistByIdWithInvalidId() {
        when(artistRepository.findById(5L)).thenReturn(Optional.empty());
        try {
            artistService.getArtistById(5L);
            fail("Expected  ObjectNotFoundException");
        } catch (ObjectNotFoundException exception) {
            assertEquals("Object with ID 5 of type Artist not found", exception.getMessage());
        }
    }

    @Test
    @DisplayName("getAlbumsByArtistId returns albums by a specific artist")
    void testGetAlbumsByArtistIdWithValidId() {
        Album album = Album.builder()
                .id(1L)
                .name("testAlbum")
                .build();
        Set<Album> expectedAlbums = Set.of(album);
        when(albumRepository.findAllByArtistId(artist.getId())).thenReturn(expectedAlbums);
        Set<Album> actualAlbums = artistService.getAlbumsByArtistId(artist.getId());
        assertEquals(expectedAlbums, actualAlbums);
    }
    @Test
    @DisplayName("getAlbumsByArtistId returns empty Set if artist id is invalid")
    void testGetAlbumsByArtistIdWithInvalidId() {
        when(albumRepository.findAllByArtistId(any())).thenReturn(new HashSet<>());
        Set<Album> actualAlbums = artistService.getAlbumsByArtistId(artist.getId());
        assertTrue(actualAlbums.isEmpty());
    }

    @Test
    @DisplayName("addArtist saves new artist to the database")
    void testAddArtist() {
        artist = Artist.builder()
                .name("testArtist")
                .imageUUID("testUUID")
                .imageUrl("testUrl")
                .build();
        when(modelMapper.map(artistDTO, Artist.class)).thenReturn(artist);
        artistService.addArtist(artistDTO);
        verify(artistRepository, times(1)).save(artistArgumentCaptor.capture());
        Artist savedArtist = artistArgumentCaptor.getValue();
        assertEquals(artistDTO.getName(), savedArtist.getName());
        assertEquals(artistDTO.getImageUrl(), savedArtist.getImageUrl());
        assertEquals(artistDTO.getImageUUID(), savedArtist.getImageUUID());
        assertTrue(savedArtist.getAlbums().isEmpty());
        assertTrue(savedArtist.getSongs().isEmpty());
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        assertEquals(currentDateTime, savedArtist.getCreated());
        assertEquals(currentDateTime, savedArtist.getModified());

    }

    @Test
    @DisplayName("removeArtist removes the artist from the database")
    void testRemoveArtist() {
        Album album = new Album();
        Album album2 = new Album();
        Song song = new Song();
        Song song2 = new Song();
        artist.setAlbums(new HashSet<>(Set.of(album, album2)));
        artist.setSongs(new HashSet<>(Set.of(song, song2)));
        when(artistRepository.findById(artist.getId())).thenReturn(Optional.of(artist));
        artistService.removeArtist(artist.getId());
        verify(artistRepository, times(1)).delete(artist);
    }


}
