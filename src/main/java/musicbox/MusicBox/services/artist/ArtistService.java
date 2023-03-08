package musicbox.MusicBox.services.artist;

import jakarta.transaction.Transactional;
import musicbox.MusicBox.model.dto.ArtistDTO;
import musicbox.MusicBox.model.entity.Artist;
import musicbox.MusicBox.repositories.ArtistRepository;
import musicbox.MusicBox.repositories.PlaylistRepository;
import musicbox.MusicBox.repositories.SongRepository;
import musicbox.MusicBox.services.album.AlbumService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final ModelMapper modelMapper;
    private final SongRepository songRepository;
private final PlaylistRepository playlistRepository;
    public ArtistService(ArtistRepository artistRepository, ModelMapper modelMapper, SongRepository songRepository, PlaylistRepository playlistRepository) {
        this.artistRepository = artistRepository;
        this.modelMapper = modelMapper;
        this.songRepository = songRepository;
        this.playlistRepository = playlistRepository;
    }

    public List<Artist> getArtists() {
        return this.artistRepository.findAll();
    }

    public List<Artist> getHomeArtists() {
        return this.artistRepository.findAll().stream().limit(6).toList();
    }

    public Artist findById(Long id) {
        return this.artistRepository.findById(id).orElse(null);
    }

    public void addArtist(ArtistDTO artistDTO) {
        Artist artist = this.modelMapper.map(artistDTO, Artist.class);
        artist.setCreated(LocalDateTime.now());
        artist.setModified(LocalDateTime.now());
        artist.setAlbums(new HashSet<>());
        artist.setSongs(new HashSet<>());
        this.modelMapper.map(artist, Artist.class);
        this.artistRepository.save(artist);

    }

    @Transactional
    public void removeArtist(Long id) {
        Artist artist = this.artistRepository.findById(id).orElse(null);
        AlbumService.removeSongsFromMappedEntity(artist.getSongs(), this.songRepository, this.playlistRepository);
        this.artistRepository.deleteById(id);
    }

}
