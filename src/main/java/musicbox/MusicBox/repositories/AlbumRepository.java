package musicbox.MusicBox.repositories;

import musicbox.MusicBox.model.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface AlbumRepository extends JpaRepository<Album, Long> {

}