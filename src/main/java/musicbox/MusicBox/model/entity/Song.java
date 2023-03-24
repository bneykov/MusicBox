package musicbox.MusicBox.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import musicbox.MusicBox.model.enums.Genre;

import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "songs")
public class Song extends BaseImageEntity {

    @Column
    private String path;

    @NotNull
    @Column
    private int duration;

    @NotNull
    @Column
    @Enumerated(EnumType.STRING)
    private Genre genre;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "song_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id"))
    private Set<Artist> artists;

    @ManyToMany(mappedBy = "songs")
    private Set<Playlist> playlists;

    @ManyToOne(fetch = FetchType.EAGER)
    private Album album;


    public String getArtistsNames() {
        StringBuilder stringBuilder = new StringBuilder();
        this.artists.forEach(artist -> stringBuilder.append(artist.getName()).append(", "));
        return stringBuilder.substring(0, stringBuilder.length() - 2);
    }


    public void setDefaultImage() {
        if (this.getImageUrl() == null) {
            this.setImageUrl("https://res.cloudinary.com/bneikov/image/upload/v1678813917/generic_song_ahnwnj.jpg");
        }
    }

    public String getFormattedDuration() {
        int minutes = duration / 60;
        int seconds = duration % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }


}
