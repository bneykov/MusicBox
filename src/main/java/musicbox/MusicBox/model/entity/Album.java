package musicbox.MusicBox.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import musicbox.MusicBox.constants.DefaultImageURLs;

import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "albums")
public class Album extends BaseImageEntity {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id"))
    private Set<Artist> artists;

    @OneToMany(mappedBy = "album", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Song> songs;

    public String getArtistsNames() {
        StringBuilder stringBuilder = new StringBuilder();
        this.artists.forEach(artist -> stringBuilder.append(artist.getName()).append(", "));
        return stringBuilder.substring(0, stringBuilder.length() - 2);
    }

    public void setDefaultImage() {
        if (this.getImageUrl() == null) {
            this.setImageUrl(DefaultImageURLs.DEFAULT_ALBUM_IMAGE_URL);
        }
    }

}
