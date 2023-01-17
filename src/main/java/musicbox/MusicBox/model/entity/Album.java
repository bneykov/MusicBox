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
@Table(name = "albums")
public class Album extends BaseEntity {
    @NotNull
    @Column
    private String name;
    @Column
    private String cover;

    @ManyToOne
    private Artist artist;

    @OneToMany(mappedBy = "album")
    private Set<Song> songs;

    public Album() {
        this.songs = new HashSet<>();
    }
}
