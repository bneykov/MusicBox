package musicbox.MusicBox.repository;

import musicbox.MusicBox.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}