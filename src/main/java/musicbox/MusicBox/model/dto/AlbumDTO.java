package musicbox.MusicBox.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import musicbox.MusicBox.validation.annotation.ValidImageFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDTO {
    @NotNull
    @Size(min = 3, max = 20, message = "Album name length must be between 3 and 20 characters")
    private String name;
    @ValidImageFormat
    private MultipartFile image;
    private String imageUrl;

    private String imageUUID;
    @NotEmpty(message = "Please select at least one artist")
    private Set<Long> artists;


}
