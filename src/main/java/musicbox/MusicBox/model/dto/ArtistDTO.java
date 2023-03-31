package musicbox.MusicBox.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import musicbox.MusicBox.utils.validation.annotation.ImageNotEmpty;
import musicbox.MusicBox.utils.validation.annotation.UniqueArtistName;
import musicbox.MusicBox.utils.validation.annotation.ValidImageFormat;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
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

    @NotNull
    @Size(min = 10, max = 1000, message = "Description length must be between 10 and 1000 characters long")
    private String description;
    private String imageUrl;

    private String imageUUID;
}

