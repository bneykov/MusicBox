package musicbox.MusicBox.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.util.Set;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "playlists")
public class Playlist extends BaseEntity {

    @NotNull
    @Column
    private String name;

    @Column
    private String imageUrl;

    @ManyToOne
    private UserEntity userEntity;

    @ManyToMany(fetch = FetchType.EAGER)

    private Set<Song> songs;

    @PrePersist
    public void setDefaultValue() {
        if (this.imageUrl == null) {
            this.imageUrl = "/images/generic_playlist.jpg";
        }
    }

    public String getPlaylistDurationFormat() {
        long seconds = this.songs.stream().mapToLong(Song::getDuration).sum();
        Duration duration = Duration.ofSeconds(seconds);
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        seconds = duration.toSeconds() % 60;
        if (days > 0) {
            return String.format("%dd %02dh %02dm %02ds", days, hours, minutes, seconds);
        } else if (hours > 0) {
            return String.format("%02dh %02dm %02ds", hours, minutes, seconds);
        } else {
            return String.format("%02dm %02ds", minutes, seconds);
        }
    }

}
