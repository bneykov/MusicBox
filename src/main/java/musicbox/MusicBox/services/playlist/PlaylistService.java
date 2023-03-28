package musicbox.MusicBox.services.playlist;

import jakarta.transaction.Transactional;
import musicbox.MusicBox.model.dto.PlaylistDTO;
import musicbox.MusicBox.model.entity.Playlist;
import musicbox.MusicBox.model.entity.Song;
import musicbox.MusicBox.model.entity.UserEntity;
import musicbox.MusicBox.repositories.PlaylistRepository;
import musicbox.MusicBox.repositories.SongRepository;
import musicbox.MusicBox.repositories.UserRepository;
import musicbox.MusicBox.services.user.CustomUserDetails;
import musicbox.MusicBox.utils.errors.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service

public class PlaylistService {
    private final ModelMapper modelMapper;
    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;

    public Set<Playlist> getUserPlaylists(Long id) {
        return this.playlistRepository.findAllByUserEntityId(id);
    }

    public Playlist getPlaylistById(Long id) {
        return this.playlistRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, "Playlist"));
    }

    public PlaylistService(ModelMapper modelMapper, PlaylistRepository playlistRepository,
                           UserRepository userRepository, SongRepository songRepository) {
        this.modelMapper = modelMapper;
        this.playlistRepository = playlistRepository;
        this.userRepository = userRepository;
        this.songRepository = songRepository;

    }

    @Transactional
    public void addPlaylist(PlaylistDTO playlistDTO, CustomUserDetails userDetails) {
        Playlist playlist = this.modelMapper.map(playlistDTO, Playlist.class);
        UserEntity owner = this.userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ObjectNotFoundException(userDetails.getId(), "User"));
        playlist.setUserEntity(owner);
        playlist.setCreated(LocalDateTime.now());
        playlist.setModified(LocalDateTime.now());
        playlist.setSongs(new HashSet<>());
        this.playlistRepository.save(playlist);
    }

    public void addSongToPlaylist(Long playlistId, Long songId) {
        Playlist playlist = this.getPlaylistById(playlistId);
        Song song = this.songRepository.findById(songId)
                .orElseThrow(() -> new ObjectNotFoundException(songId, "Song"));
        playlist.getSongs().add(song);
        playlist.setModified(LocalDateTime.now());
        this.playlistRepository.save(playlist);
    }

    @Transactional
    public void removeSongFromPlaylist(Long playlistId, Long songId) {
        Playlist playlist = this.getPlaylistById(playlistId);
        Song song = this.songRepository.findById(songId)
                .orElseThrow(() -> new ObjectNotFoundException(songId, "Song"));
        playlist.getSongs().remove(song);
        playlist.setModified(LocalDateTime.now());
        this.playlistRepository.save(playlist);
    }

    public void removePlaylist(Long id) {
        this.playlistRepository.deleteById(id);
    }

    public boolean isOwner(String username, Long id){
        return playlistRepository.
                findById(id).
                filter(playlist -> playlist.getUserEntity().getUsername().equals(username)).
                isPresent();
    }

}
