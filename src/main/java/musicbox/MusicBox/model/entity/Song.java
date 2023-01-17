package musicbox.MusicBox.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import musicbox.MusicBox.model.enums.Genre;

import java.io.FileReader;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "songs")
public class Song extends BaseEntity {
    @NotNull
    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String lyrics;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String path;

    @NotNull
    @Column
    private LocalTime length;
    

    @NotNull
    @Column
    @Enumerated(EnumType.STRING)
    private Genre genre;
    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "song_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id"))
    private Set<Artist> artists;

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "song_id"),
    inverseJoinColumns = @JoinColumn(name = "playlist_id"))
    private Set<Playlist> playlists;

    @ManyToOne
    private Album album;

    public Song() {
        this.artists = new HashSet<>();
        this.playlists = new HashSet<>();
    }
}
