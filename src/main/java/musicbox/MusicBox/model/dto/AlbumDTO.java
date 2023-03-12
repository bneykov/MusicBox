package musicbox.MusicBox.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import musicbox.MusicBox.validation.annotation.UniqueUsername;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDTO {
    @NotNull
    @Size(min = 3, max = 20, message = "Album name length must be between 3 and 20 characters")
    private String name;
    private String imageUrl;
    @NotEmpty(message = "Please select at least one artist")
    private Set<Long> artists;
}
