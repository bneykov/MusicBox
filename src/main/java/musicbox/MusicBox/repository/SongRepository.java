package musicbox.MusicBox.repository;

import musicbox.MusicBox.model.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {
}