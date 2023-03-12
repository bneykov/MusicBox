package musicbox.MusicBox.repositories;

import musicbox.MusicBox.model.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository

public interface AlbumRepository extends JpaRepository<Album, Long> {
    @Query("SELECT a FROM Album a JOIN a.artists artists WHERE artists.id = :id")
    Set<Album> findAllByArtistId(@Param("id") Long id);

}