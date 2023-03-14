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
public class Artist extends BaseImage {

    @ManyToMany(mappedBy = "artists", cascade = CascadeType.ALL)
    private Set<Song> songs;

    @ManyToMany(mappedBy = "artists", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Album> albums;

    public void removeSong(Song song){
        this.songs.remove(song);
        song.getArtists().remove(this);
    }




}
