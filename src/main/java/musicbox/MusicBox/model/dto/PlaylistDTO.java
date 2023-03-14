package musicbox.MusicBox.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistDTO {
    @NotNull
    @Size(min = 3, max = 20, message = "Playlist name length must be between 3 and 20 characters")
    private String name;

    private MultipartFile image;

    private String imageUrl;

    private String imageUUID;


}
