package musicbox.MusicBox.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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
@Table(name = "albums")
public class Album extends BaseEntity {
    @NotEmpty
    @Column
    private String name;
    @Column(columnDefinition = "TEXT")
    private String imageUrl;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id"))
    private Set<Artist> artists;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Song> songs;

    public String getArtistsNames(){
        StringBuilder stringBuilder = new StringBuilder();
        this.artists.forEach(artist -> stringBuilder.append(artist.getName()).append(", "));
       return stringBuilder.substring(0, stringBuilder.length() - 2);
    }
    @PrePersist
    public void setDefaultValue(){
        if (this.imageUrl == null) {
            this.imageUrl = "/images/generic_album.png";
        }
    }

}
