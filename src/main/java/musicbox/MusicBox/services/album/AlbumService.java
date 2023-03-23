package musicbox.MusicBox.services.album;

import jakarta.transaction.Transactional;
import musicbox.MusicBox.model.dto.AlbumDTO;
import musicbox.MusicBox.model.entity.Album;
import musicbox.MusicBox.model.entity.Artist;
import musicbox.MusicBox.model.entity.Song;
import musicbox.MusicBox.repositories.AlbumRepository;
import musicbox.MusicBox.repositories.ArtistRepository;
import musicbox.MusicBox.repositories.SongRepository;
import musicbox.MusicBox.services.song.SongService;
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
    private final ArtistRepository artistRepository;

    public AlbumService(AlbumRepository albumRepository, ModelMapper modelMapper, SongRepository songRepository,
                        ArtistRepository artistRepository) {
        this.albumRepository = albumRepository;
        this.modelMapper = modelMapper;
        this.songRepository = songRepository;
        this.artistRepository = artistRepository;
    }

    public List<Album> getAlbums() {
        return this.albumRepository.findAll();
    }

    public List<Album> getHomeAlbums() {
        return this.albumRepository.findAll().stream().limit(6).toList();
    }

    public Set<Song> getSongsByAlbumId(Long id) {
        return this.songRepository.findAllByAlbumId(id);
    }

    public Album getAlbumById(Long id) {
        return this.albumRepository.findById(id).orElse(null);
    }

    public void addAlbum(AlbumDTO albumDTO) {
        Album album = this.modelMapper.map(albumDTO, Album.class);
        album.setCreated(LocalDateTime.now());
        album.setModified(LocalDateTime.now());
        album.setSongs(new HashSet<>());
        album.setArtists(new HashSet<>());
        List<Artist> artistsToAdd = this.artistRepository.findAllById(albumDTO.getArtists());
        artistsToAdd.forEach(artist -> album.getArtists().add(artist));
        this.modelMapper.map(album, Album.class);
        this.albumRepository.save(album);
    }


    public void removeAllAlbumsFromArtist(Set<Album> albums) {
        List<Long> albumIds = albums.stream().map(Album::getId).toList();
        List<Album> albumsToDelete = this.albumRepository.findAllById(albumIds);
        albumsToDelete.forEach(album -> {
            removeSongsFromLinkedEntities(album.getSongs());
            unlinkArtistsFromAlbum(album);
        });
        this.albumRepository.deleteAll(albumsToDelete);
    }

    @Transactional
    public void removeAlbum(Long id) {
        Album album = this.getAlbumById(id);
        removeSongsFromLinkedEntities(album.getSongs());
        unlinkArtistsFromAlbum(album);
        this.albumRepository.delete(album);
    }

    public void unlinkArtistsFromAlbum(Album album) {
        List<Long> artistIds = album.getArtists().stream().map(Artist::getId).toList();
        List<Artist> artistsToUnlink = this.artistRepository.findAllById(artistIds);
        artistsToUnlink.forEach(artist -> artist.getAlbums().remove(album));
        this.artistRepository.saveAll(artistsToUnlink);
    }

    public void removeSongsFromLinkedEntities(Set<Song> songs) {
        List<Long> songIds = songs.stream().map(Song::getId).toList();
        List<Song> songsToDelete = this.songRepository.findAllById(songIds);
        songsToDelete = songsToDelete.stream().map(SongService::getSongWithoutRelations).toList();
        this.songRepository.deleteAll(songsToDelete);
    }

}
