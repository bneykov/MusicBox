package musicbox.MusicBox.repositories;

import jakarta.validation.constraints.NotNull;
import musicbox.MusicBox.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}