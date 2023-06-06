package musicbox.MusicBox.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import musicbox.MusicBox.constants.DefaultImageURLs;

import java.time.Duration;
import java.util.Set;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "playlists")
public class Playlist extends BaseImageEntity {
    @Column(unique = true)
    private String link;
    @ManyToOne
    private UserEntity userEntity;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Song> songs;

    @Override
    public void prePersist() {
        if (this.getImageUrl() == null) {
            this.setImageUrl(DefaultImageURLs.DEFAULT_PLAYLIST_IMAGE_URL);
        }
    }

    @PostPersist
    private void setPlaylistLink(){
        this.link = "http://localhost:8000/playlists/" + this.getId();
    }

    //Get playlist total duration formatted
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
