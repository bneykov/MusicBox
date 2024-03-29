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
import musicbox.MusicBox.utils.errors.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    //Return 6 albums for the home page
    public List<Album> getHomeAlbums() {
        return this.albumRepository.findAll().stream().limit(6).toList();
    }

    public Set<Song> getSongsByAlbumId(Long id) {
        return this.songRepository.findAllByAlbumId(id);
    }

    public Album getAlbumById(Long id) {
        return this.albumRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, "Album"));
    }
    //Save album with the data from the DTO
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

    //Remove all albums related to an artist
    public void removeAllAlbumsFromArtist(Set<Album> albums) {
        List<Long> albumIds = albums.stream().map(Album::getId).toList();
        List<Album> albumsToDelete = this.albumRepository.findAllById(albumIds);
        albumsToDelete.forEach(album -> {
            removeSongsFromLinkedEntities(album.getSongs());
            unlinkArtistsFromAlbum(album);
        });
        this.albumRepository.deleteAll(albumsToDelete);
    }
    //Remove album from database
    @Transactional
    public void removeAlbum(Long id) {
        Album album = this.getAlbumById(id);
        removeSongsFromLinkedEntities(album.getSongs());
        unlinkArtistsFromAlbum(album);
        this.albumRepository.delete(album);
    }
    //Remove all artists related to an album
    public void unlinkArtistsFromAlbum(Album album) {
        Set<Artist> artistsToUnlink = album.getArtists();
        artistsToUnlink.forEach(artist -> artist.getAlbums().remove(album));
        this.artistRepository.saveAll(artistsToUnlink);
    }
    //Remove songs' relations and deletes them
    public void removeSongsFromLinkedEntities(Set<Song> songs) {
        songs = songs.stream().map(SongService::getSongWithoutRelations).collect(Collectors.toSet());
        this.songRepository.deleteAll(songs);
    }

}
