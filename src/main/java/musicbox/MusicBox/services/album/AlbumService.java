package musicbox.MusicBox.services.album;

import jakarta.transaction.Transactional;
import musicbox.MusicBox.model.dto.AlbumDTO;
import musicbox.MusicBox.model.entity.Album;
import musicbox.MusicBox.model.entity.Artist;
import musicbox.MusicBox.model.entity.Playlist;
import musicbox.MusicBox.model.entity.Song;
import musicbox.MusicBox.repositories.AlbumRepository;
import musicbox.MusicBox.repositories.ArtistRepository;
import musicbox.MusicBox.repositories.PlaylistRepository;
import musicbox.MusicBox.repositories.SongRepository;
import musicbox.MusicBox.services.artist.ArtistService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final ModelMapper modelMapper;
    private final SongRepository songRepository;
    private final ArtistService artistService;
    private final PlaylistRepository playlistRepository;
    private final ArtistRepository artistRepository;

    public AlbumService(AlbumRepository albumRepository, ModelMapper modelMapper, SongRepository songRepository, ArtistService artistService, PlaylistRepository playlistRepository, ArtistRepository artistRepository) {
        this.albumRepository = albumRepository;
        this.modelMapper = modelMapper;
        this.songRepository = songRepository;
        this.artistService = artistService;
        this.playlistRepository = playlistRepository;
        this.artistRepository = artistRepository;
    }

    public List<Album> getAlbums() {
        return this.albumRepository.findAll();
    }

    public List<Album> getHomeAlbums() {
        return this.albumRepository.findAll().stream().limit(6).toList();
    }
    public Set<Song> getSongsByAlbumId(Long id){
        return this.songRepository.findAllByAlbumId(id);
    }
    public Album getAlbumById(Long id){
        return this.albumRepository.findById(id).orElse(null);
    }

    public void addAlbum(AlbumDTO albumDTO) {
        Album album = this.modelMapper.map(albumDTO, Album.class);
        album.setCreated(LocalDateTime.now());
        album.setModified(LocalDateTime.now());
        album.setSongs(new HashSet<>());
        album.setArtists(new HashSet<>());
        albumDTO.getArtists().forEach(artistId -> {
            Artist artist = this.artistService.getArtistById(artistId);
            album.getArtists().add(artist);
        });
        this.modelMapper.map(album, Album.class);
        this.albumRepository.save(album);
    }

    @Transactional
    public void removeAlbum(Long id) {
        Album album = this.albumRepository.findById(id).orElse(null);
        removeSongsFromMappedEntity(album.getSongs(), this.songRepository, this.playlistRepository);
        album.getArtists().forEach(entry -> {
            Artist artist = this.artistService.getArtistById(entry.getId());
            artist.getAlbums().remove(album);
            this.artistRepository.save(artist);
        });
        this.albumRepository.deleteById(id);
    }

    public static void removeSongsFromMappedEntity(Set<Song> songs, SongRepository songRepository, PlaylistRepository playlistRepository) {

        songs.forEach(element -> {
            Song song = songRepository.findById(element.getId()).orElse(null);
            song.getPlaylists().forEach(entry -> {
                Playlist playlist = playlistRepository.findById(entry.getId()).orElse(null);
                playlist.getSongs().remove(song);
                playlistRepository.save(playlist);
            });
            song.getPlaylists().clear();
            song.getArtists().clear();
            song.setAlbum(null);
            songRepository.save(song);

        });

    }
}
