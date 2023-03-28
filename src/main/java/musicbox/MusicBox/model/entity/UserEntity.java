package musicbox.MusicBox.model.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import musicbox.MusicBox.constants.DefaultImageURLs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity extends BaseImageEntity {
    @NotNull
    @Column(unique = true)
    private String username;
    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    @Column
    private String password;

    @Column
    private LocalDateTime lastLoggedIn;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<UserRole> roles;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE)
    private Set<Playlist> playlists;


    public void setDefaultImage() {
        if (this.getImageUrl() == null) {
            this.setImageUrl(DefaultImageURLs.DEFAULT_USER_IMAGE_URL);
        }
    }

    public String getLastLoggedIn() {
        return lastLoggedIn.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }
}
