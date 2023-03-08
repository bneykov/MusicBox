package musicbox.MusicBox.repositories;

import musicbox.MusicBox.model.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface SongRepository extends JpaRepository<Song, Long> {


}