package musicbox.MusicBox.services.song;


import jakarta.transaction.Transactional;
import musicbox.MusicBox.model.dto.SongDTO;
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

@Service

public class SongService {
    private final SongRepository songRepository;
    private final ModelMapper modelMapper;

    private final ArtistService artistService;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final PlaylistRepository playlistRepository;

    public SongService(SongRepository songRepository, ModelMapper modelMapper, ArtistService artistService, ArtistRepository artistRepository, AlbumRepository albumRepository, PlaylistRepository playlistRepository) {
        this.songRepository = songRepository;
        this.modelMapper = modelMapper;
        this.artistService = artistService;
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
        this.playlistRepository = playlistRepository;
    }

    public Song getSongById(Long id){
        return this.songRepository.findById(id).orElse(null);
    }

    public List<Song> getSongs(){
        return this.songRepository.findAll();
    }
    public List<Song> getHomeSongs(){
        return this.songRepository.findAll().stream().limit(6).toList();
    }
    @Transactional
    public void addSong(SongDTO songDTO){
        Song song = this.modelMapper.map(songDTO, Song.class);
        Album album = this.albumRepository.findById(songDTO.getAlbum()).orElse(null);
        song.setArtists(new HashSet<>());
        song.setAlbum(album);
        song.setCreated(LocalDateTime.now());
        song.setModified(LocalDateTime.now());
        song.setPlaylists(new HashSet<>());
        songDTO.getArtists().forEach(artistId -> {
            Artist artist = this.artistService.findById(artistId);
            song.getArtists().add(artist);
        });
        this.modelMapper.map(song, Song.class);
        this.songRepository.save(song);
    }
    @Transactional
    public void removeSong(Long id){
        Song song = this.getSongById(id);
        song.getArtists().forEach(entry -> {
            Artist artist = this.artistRepository.findById(entry.getId()).orElse(null);
            artist.getSongs().remove(song);
            this.artistRepository.save(artist);
        });
        song.getPlaylists().forEach(entry -> {
            Playlist playlist = this.playlistRepository.findById(entry.getId()).orElse(null);
            playlist.getSongs().remove(song);
            this.playlistRepository.save(playlist);
        });
        song.getArtists().clear();
        song.getPlaylists().clear();
        song.setAlbum(null);
        this.songRepository.save(song);

        this.songRepository.deleteById(id);
    }
}
