package musicbox.MusicBox.repositories;

import musicbox.MusicBox.model.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

}