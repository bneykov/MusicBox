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
public class Song extends BaseEntity {
    @NotNull
    @Column
    private String title;



    @Column(columnDefinition = "TEXT")
    private String path;

    @NotNull
    @Column
    private int duration;

    @Column
    private String imageUrl;

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

    public String getArtistsNames(){
        StringBuilder stringBuilder = new StringBuilder();
        this.artists.forEach(artist -> stringBuilder.append(artist.getName()).append(", "));
        return stringBuilder.substring(0, stringBuilder.length() - 2);
    }
    @PrePersist
    public void setDefaultValue(){
        if (this.imageUrl == null) {
            this.imageUrl = "/images/generic_song.jpg";
        }
    }
    public String getFormattedDuration(){
        int minutes = duration /60;
        int seconds = duration % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

}
