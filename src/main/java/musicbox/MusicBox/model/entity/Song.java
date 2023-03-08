package musicbox.MusicBox.model.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import musicbox.MusicBox.model.enums.Genre;

import java.io.FileReader;
import java.time.LocalTime;
import java.util.HashSet;
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
    private int length;

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

}
