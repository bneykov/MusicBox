package musicbox.MusicBox.services.album;

import musicbox.MusicBox.model.dto.AlbumDTO;
import musicbox.MusicBox.model.entity.Album;
import musicbox.MusicBox.model.entity.Song;
import musicbox.MusicBox.repositories.AlbumRepository;
import musicbox.MusicBox.repositories.ArtistRepository;
import musicbox.MusicBox.repositories.PlaylistRepository;
import musicbox.MusicBox.repositories.SongRepository;
import musicbox.MusicBox.services.artist.ArtistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlbumServiceTest {
    @Mock
    private AlbumRepository albumRepository;
    @Mock
    private SongRepository songRepository;
    @Mock
    private ArtistService artistService;
    @Mock
    private PlaylistRepository playlistRepository;
    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AlbumService albumService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        albumService =
                new AlbumService(
                        albumRepository,
                        modelMapper,
                        songRepository,
                        artistService,
                        playlistRepository,
                        artistRepository);
    }

    @Test
    @DisplayName("Test adding a new album with valid data")
    void addAlbumWithValidData() {
        AlbumDTO albumDTO = new AlbumDTO();
        albumDTO.setName("Test");
        albumDTO.setArtists(new HashSet<>());
        albumDTO.getArtists().add(1L);
        albumDTO.getArtists().add(2L);

        Album album = new Album();
        album.setName("Test");
        album.setArtists(new HashSet<>());

        when(modelMapper.map(albumDTO, Album.class)).thenReturn(album);

        albumService.addAlbum(albumDTO);

        verify(albumRepository, times(1)).save(album);
    }

    @Test
    @DisplayName("Test removeAlbum method to remove an album and its associated data")
    void removeAlbumRemovesAlbumAndAssociatedData() {
        Album album = Album.builder()
                .songs(new HashSet<>())
                .id(1L)
                .name("test")
                .artists(new HashSet<>())
                .build();
        when(albumRepository.findById(1L)).thenReturn(java.util.Optional.of(album));
        albumService.removeAlbum(1L);
        verify(albumRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Test getAlbums method to return all albums")
    void getAlbumsReturnsAllAlbums() {
        List<Album> albums = new ArrayList<>();
        albums.add(new Album());
        albums.add(new Album());
        when(albumRepository.findAll()).thenReturn(albums);
        assertEquals(albums, albumService.getAlbums());
    }


    @Test
    @DisplayName("getSongsByAlbumId returns songs for a specific album")
    void getSongsByAlbumIdReturnsSongsForAlbum() {

        Set<Song> songs = new HashSet<>();
        when(songRepository.findAllByAlbumId(1L)).thenReturn(songs);
        Set<Song> actual = albumService.getSongsByAlbumId(1L);
        assertEquals(songs, actual);
    }

    @Test
    @DisplayName("getHomeAlbums returns limited number of albums")
    void getHomeAlbumsReturnsLimitedAlbums() {
        List<Album> albums = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            albums.add(new Album());
        }
        when(albumRepository.findAll()).thenReturn(albums);

        List<Album> result = albumService.getHomeAlbums();

        assertEquals(6, result.size());
    }
}