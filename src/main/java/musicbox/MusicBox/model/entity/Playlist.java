package musicbox.MusicBox.model.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "playlists")
public class Playlist extends BaseEntity {

    @Nonnull
    @Column
    private String name;

    @ManyToOne
    private User user;

    @ManyToMany(mappedBy = "playlists")
    private Set<Song> songs;
}
