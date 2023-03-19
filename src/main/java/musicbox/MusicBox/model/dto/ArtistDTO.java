package musicbox.MusicBox.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import musicbox.MusicBox.validation.annotation.ImageNotEmpty;
import musicbox.MusicBox.validation.annotation.UniqueArtistName;
import musicbox.MusicBox.validation.annotation.ValidImageFormat;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDTO {
    @NotNull
    @UniqueArtistName
    @Size(min = 3, max = 30, message = "Artist name length must be at between 3 and 30 characters")
    private String name;

    @ImageNotEmpty
    @ValidImageFormat
    private MultipartFile image;

    private String imageUrl;

    private String imageUUID;
}

