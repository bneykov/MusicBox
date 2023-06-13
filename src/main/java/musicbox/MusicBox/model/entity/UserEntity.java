package musicbox.MusicBox.model.entity;


import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import musicbox.MusicBox.constants.DefaultImageURLs;
import musicbox.MusicBox.utils.token.SecretKeyOperations;

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

    @Column
    @NotNull
    private String secretKey;


    @ManyToMany(fetch = FetchType.EAGER)
    private List<UserRole> roles;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE)
    private Set<Playlist> playlists;


    @SneakyThrows
    public void prePersist() {
        this.secretKey = SecretKeyOperations.encrypt(Keys.secretKeyFor(SignatureAlgorithm.HS256));
        if (this.getImageUrl() == null || this.getImageUrl().isBlank()) {
            this.setImageUrl(DefaultImageURLs.DEFAULT_USER_IMAGE_URL);
        }
    }


    public String getLastLoggedIn() {
        return lastLoggedIn.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }

    public String getAccountTerminationDate(){
        return lastLoggedIn.plusMonths(6).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }
}
