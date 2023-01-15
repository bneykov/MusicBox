package musicbox.MusicBox.repository;

import musicbox.MusicBox.model.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {
}