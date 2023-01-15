package musicbox.MusicBox.model.entity;

import jakarta.persistence.*;
import musicbox.MusicBox.model.enums.Roles;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Roles role;
}
