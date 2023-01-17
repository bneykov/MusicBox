package musicbox.MusicBox.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "artists")
public class Artist extends Person {

    @ManyToMany(mappedBy = "artists")
    private Set<Song> songs;

    @OneToMany(mappedBy = "artist")
    private Set<Album> albums;

    public Artist() {
        this.songs = new HashSet<>();
        this.albums = new HashSet<>();
    }
}
