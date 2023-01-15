package musicbox.MusicBox.repository;

import musicbox.MusicBox.model.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
}