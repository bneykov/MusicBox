package musicbox.MusicBox.repository;

import musicbox.MusicBox.model.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
}