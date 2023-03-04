package musicbox.MusicBox.repositories;

import musicbox.MusicBox.model.entity.Artist;
import musicbox.MusicBox.model.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository

public interface SongRepository extends JpaRepository<Song, Long> {


}