package musicbox.MusicBox.model.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity extends Person {
    @NotNull
    @Column(unique = true)
    private String username;
    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    @Column
    private String password;


    @ManyToMany(fetch = FetchType.EAGER)

    private List<UserRole> roles;

    @OneToMany(mappedBy = "userEntity")
    private Set<Playlist> playlists;

}
