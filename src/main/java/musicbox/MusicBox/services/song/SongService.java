package musicbox.MusicBox.services.song;


import jakarta.transaction.Transactional;
import musicbox.MusicBox.model.dto.SongDTO;
import musicbox.MusicBox.model.entity.Album;
import musicbox.MusicBox.model.entity.Song;
import musicbox.MusicBox.repositories.AlbumRepository;
import musicbox.MusicBox.repositories.SongRepository;
import musicbox.MusicBox.utils.errors.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service

public class SongService {
    private final SongRepository songRepository;
    private final ModelMapper modelMapper;
    private final AlbumRepository albumRepository;

    public SongService(SongRepository songRepository, ModelMapper modelMapper, AlbumRepository albumRepository) {
        this.songRepository = songRepository;
        this.modelMapper = modelMapper;
        this.albumRepository = albumRepository;

    }

    public static Song getSongWithoutRelations(Song song) {
        song.getArtists().forEach(artist -> artist.getSongs().remove(song));
        song.getPlaylists().forEach(playlist -> playlist.getSongs().remove(song));
        song.getArtists().clear();
        song.getPlaylists().clear();
        song.setAlbum(null);
        return song;
    }

    public Song getSongById(Long id) {
        return this.songRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, "Song"));
    }

    public List<Song> getSongs() {
        return this.songRepository.findAll();
    }

    public List<Song> getHomeSongs() {
        return this.songRepository.findAll().stream().limit(6).toList();
    }

    @Transactional
    public void addSong(SongDTO songDTO) {
        Song song = this.modelMapper.map(songDTO, Song.class);
        Album album = this.albumRepository.findById(songDTO.getAlbum())
                .orElseThrow(() -> new ObjectNotFoundException(songDTO.getAlbum(), "Album"));
        song.setArtists(new HashSet<>());
        song.setAlbum(album);
        song.setCreated(LocalDateTime.now());
        song.setModified(LocalDateTime.now());
        song.setPlaylists(new HashSet<>());
        album.getArtists().forEach(artist -> song.getArtists().add(artist));
        this.songRepository.save(song);
    }

    @Transactional
    public void removeSongConnections(Long id) {
        Song song = this.getSongById(id);
        this.songRepository.save(getSongWithoutRelations(song));
    }

    public void removeSong(Long id) {
        this.songRepository.deleteById(id);
    }
}
