package musicbox.MusicBox.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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


}
