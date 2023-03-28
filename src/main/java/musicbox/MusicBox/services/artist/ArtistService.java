package musicbox.MusicBox.services.artist;

import jakarta.transaction.Transactional;
import musicbox.MusicBox.model.dto.ArtistDTO;
import musicbox.MusicBox.model.entity.Album;
import musicbox.MusicBox.model.entity.Artist;
import musicbox.MusicBox.repositories.AlbumRepository;
import musicbox.MusicBox.repositories.ArtistRepository;
import musicbox.MusicBox.services.album.AlbumService;
import musicbox.MusicBox.utils.errors.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final ModelMapper modelMapper;
    private final AlbumRepository albumRepository;
    private final AlbumService albumService;

    public ArtistService(ArtistRepository artistRepository, ModelMapper modelMapper,
                         AlbumRepository albumRepository, AlbumService albumService) {
        this.artistRepository = artistRepository;
        this.modelMapper = modelMapper;
        this.albumRepository = albumRepository;
        this.albumService = albumService;
    }

    public List<Artist> getArtists() {
        return this.artistRepository.findAll();
    }

    public List<Artist> getHomeArtists() {
        return this.artistRepository.findAll().stream().limit(6).toList();
    }

    public Artist getArtistById(Long id) {
        return this.artistRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, "Artist"));
    }

    public Set<Album> getAlbumsByArtistId(Long id) {
        return this.albumRepository.findAllByArtistId(id);
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
        Artist artist = this.getArtistById(id);
        this.albumService.removeAllAlbumsFromArtist(artist.getAlbums());
        this.artistRepository.delete(artist);
    }

}
