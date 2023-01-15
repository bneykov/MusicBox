package musicbox.MusicBox.model.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import musicbox.MusicBox.model.enums.Genre;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "songs")
public class Song extends BaseEntity {
    @Nonnull
    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String lyrics;

    @Nonnull
    @Column(columnDefinition = "TEXT")
    private String path;

    @Nonnull
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
}
