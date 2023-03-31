package musicbox.MusicBox.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "artists")
public class Artist extends BaseImageEntity {
    @Column(length = 100000)
    private String description;

    @ManyToMany(mappedBy = "artists", cascade = CascadeType.ALL)
    private Set<Song> songs;

    @ManyToMany(mappedBy = "artists", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Album> albums;


}
