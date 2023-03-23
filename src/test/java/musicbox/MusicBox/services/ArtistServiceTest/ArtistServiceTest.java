package musicbox.MusicBox.services.ArtistServiceTest;

import musicbox.MusicBox.model.dto.ArtistDTO;
import musicbox.MusicBox.model.entity.Album;
import musicbox.MusicBox.model.entity.Artist;
import musicbox.MusicBox.model.entity.Song;
import musicbox.MusicBox.repositories.AlbumRepository;
import musicbox.MusicBox.repositories.ArtistRepository;
import musicbox.MusicBox.services.album.AlbumService;
import musicbox.MusicBox.services.artist.ArtistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
    @DisplayName("getArtistById returns null when given invalid id")
    void testGetArtistByIdWithInvalidId() {
        Artist actual = artistService.getArtistById(1L);
        assertNull(actual);
    }

    @Test
    @DisplayName("getAlbumsByArtistId returns albums by a specific artist")
    void testGetAlbumsByArtistId() {
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
    @DisplayName("addArtist saves new artist to the database")
    void testAddArtist() {
        when(modelMapper.map(artistDTO, Artist.class)).thenReturn(artist);
        artistService.addArtist(artistDTO);
        verify(artistRepository, times(1)).save(artist);
        assertEquals(artistDTO.getName(), artist.getName());
        assertEquals(artistDTO.getImageUrl(), artist.getImageUrl());
        assertEquals(artistDTO.getImageUUID(), artist.getImageUUID());
        assertTrue(artist.getAlbums().isEmpty());
        assertTrue(artist.getSongs().isEmpty());
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        assertEquals(currentDateTime, artist.getCreated());
        assertEquals(currentDateTime, artist.getModified());

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
