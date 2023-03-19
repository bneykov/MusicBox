package musicbox.MusicBox.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.IOException;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "artists")
public class Artist extends BaseImageEntity {

    @ManyToMany(mappedBy = "artists", cascade = CascadeType.ALL)
    private Set<Song> songs;

    @ManyToMany(mappedBy = "artists", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Album> albums;

    @Override
    public void preRemove() throws IOException {
        super.preRemove();
    }
}
