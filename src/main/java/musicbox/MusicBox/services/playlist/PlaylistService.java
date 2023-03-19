package musicbox.MusicBox.services.playlist;

import jakarta.transaction.Transactional;
import musicbox.MusicBox.services.user.CustomUserDetails;
import musicbox.MusicBox.model.dto.PlaylistDTO;
import musicbox.MusicBox.model.entity.Playlist;
import musicbox.MusicBox.model.entity.Song;
import musicbox.MusicBox.model.entity.UserEntity;
import musicbox.MusicBox.repositories.PlaylistRepository;
import musicbox.MusicBox.services.song.SongService;
import musicbox.MusicBox.services.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service

public class PlaylistService {
    private final ModelMapper modelMapper;
    private final PlaylistRepository playlistRepository;
    private final UserService userService;
    private final SongService songService;

    public Set<Playlist> getUserPlaylists(Long id) {
        return this.playlistRepository.findAllByUserEntityId(id);
    }

    public Playlist getPlaylistById(Long id) {
        return this.playlistRepository.findById(id).orElse(null);
    }

    @Autowired

    public PlaylistService(ModelMapper modelMapper, PlaylistRepository playlistRepository, UserService userService, SongService songService) {
        this.modelMapper = modelMapper;
        this.playlistRepository = playlistRepository;
        this.userService = userService;
        this.songService = songService;

    }

    @Transactional
    public void addPlaylist(PlaylistDTO playlistDTO, CustomUserDetails userDetails) {
        Playlist playlist = this.modelMapper.map(playlistDTO, Playlist.class);
        UserEntity owner = this.userService.getUserById(userDetails.getId());
        playlist.setUserEntity(owner);
        playlist.setCreated(LocalDateTime.now());
        playlist.setModified(LocalDateTime.now());
        playlist.setSongs(new HashSet<>());
        this.playlistRepository.save(playlist);
    }

    public void addSongToPlaylist(Long playlistId, Long songId) {
        Playlist playlist = this.getPlaylistById(playlistId);
        Song song = this.songService.getSongById(songId);
        playlist.getSongs().add(song);
        playlist.setModified(LocalDateTime.now());
        this.playlistRepository.save(playlist);
    }

    @Transactional
    public void removeSongFromPlaylist(Long playlistId, Long songId) {
        Playlist playlist = this.getPlaylistById(playlistId);
        Song song = this.songService.getSongById(songId);
        playlist.getSongs().remove(song);
        playlist.setModified(LocalDateTime.now());
        this.playlistRepository.save(playlist);
    }

    public void removePlaylist(Long id) {
        this.playlistRepository.deleteById(id);
    }
}
