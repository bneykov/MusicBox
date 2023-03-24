package musicbox.MusicBox.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import musicbox.MusicBox.utils.validation.annotation.UniquePlaylistName;
import musicbox.MusicBox.utils.validation.annotation.ValidImageFormat;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistDTO {
    @NotNull
    @UniquePlaylistName
    @Size(min = 3, max = 20, message = "Playlist name length must be between 3 and 20 characters")
    private String name;
    @ValidImageFormat
    private MultipartFile image;

    private String imageUrl;

    private String imageUUID;


}
