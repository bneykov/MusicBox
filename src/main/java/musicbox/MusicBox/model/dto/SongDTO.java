package musicbox.MusicBox.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import musicbox.MusicBox.model.enums.Genre;
import musicbox.MusicBox.utils.validation.annotation.ValidImageFormat;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongDTO {

    @NotNull
    @Size(min = 3, max = 30, message = "Title length must be between 3 and 30 characters")
    private String name;
    @ValidImageFormat
    private MultipartFile image;

    private String imageUrl;

    private String imageUUID;
    @Positive(message = "Duration must be greater than 0")
    private int duration;

    private String path;

    @NotNull(message = "Please select genre")
    private Genre genre;

    @NotNull(message = "Please select an album")
    private Long album;
}
