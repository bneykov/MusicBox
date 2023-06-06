package musicbox.MusicBox.services.playlist;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.transaction.Transactional;
import musicbox.MusicBox.model.dto.PlaylistDTO;
import musicbox.MusicBox.model.entity.Playlist;
import musicbox.MusicBox.model.entity.Song;
import musicbox.MusicBox.model.entity.UserEntity;
import musicbox.MusicBox.repositories.PlaylistRepository;
import musicbox.MusicBox.repositories.SongRepository;
import musicbox.MusicBox.repositories.UserRepository;
import musicbox.MusicBox.services.user.CustomUserDetails;
import musicbox.MusicBox.utils.errors.InvalidTokenException;
import musicbox.MusicBox.utils.errors.ObjectNotFoundException;
import musicbox.MusicBox.utils.token.AccessTokenGenerator;
import musicbox.MusicBox.utils.token.SecretKeyOperations;
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

    public PlaylistService(ModelMapper modelMapper, PlaylistRepository playlistRepository,
                           UserRepository userRepository, SongRepository songRepository) {
        this.modelMapper = modelMapper;
        this.playlistRepository = playlistRepository;
        this.userRepository = userRepository;
        this.songRepository = songRepository;

    }

    public Set<Playlist> getUserPlaylists(Long id) {
        return this.playlistRepository.findAllByUserEntityId(id);
    }

    public Playlist getPlaylistById(Long id) {
        return this.playlistRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, "Playlist"));
    }

    //Save playlist with the data from the DTO
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

    //Remove playlist from database
    public void removePlaylist(Long id) {
        this.playlistRepository.deleteById(id);
    }

    // Check if a user is playlist's owner
    public boolean isOwner(String username, Long id) {
        return playlistRepository.
                findById(id).
                filter(playlist -> playlist.getUserEntity().getUsername().equals(username)).
                isPresent();
    }

    public String createShareableLink(Long id) {
        Playlist playlist = this.getPlaylistById(id);
        String accessToken = AccessTokenGenerator.generateAccessToken(id, playlist.getUserEntity().getSecretKey());
        return playlist.getLink() + "/" + accessToken;
    }

    public boolean isValidToken(Playlist playlist, String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SecretKeyOperations.decrypt(playlist.getUserEntity().getSecretKey()))
                    .build().parseClaimsJws(token)
                    .getBody();

            String playlistId = claims.getSubject();

            if (playlistId.equals(playlist.getId().toString())) {
                return true;
            }
        } catch (SignatureException | MalformedJwtException ex) {
            throw new InvalidTokenException();
        }
        throw new ObjectNotFoundException(playlist.getId(), "Playlist");
    }

}
