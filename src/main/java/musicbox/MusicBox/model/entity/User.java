package musicbox.MusicBox.model.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends Person {

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    @Column
    private String password;

    @ManyToOne
    private Role role;

    @OneToMany(mappedBy = "user")
    private Set<Playlist> playlists;

    public User() {
        this.playlists = new HashSet<>();
    }
}
