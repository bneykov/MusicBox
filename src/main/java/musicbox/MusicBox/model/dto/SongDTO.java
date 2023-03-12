package musicbox.MusicBox.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import musicbox.MusicBox.model.entity.Artist;
import musicbox.MusicBox.model.enums.Genre;
import org.springframework.format.annotation.NumberFormat;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SongDTO {

    @NotNull
    @Size(min = 3, max = 30, message = "Title length must be between 3 and 30 characters")
    private String title;
    @Positive(message = "Duration must be greater than 0")
    private int duration;
    @NotEmpty(message = "Please select at least one artist")
    private Set<Long> artists;

    private String path;

    @NotNull(message = "Please select genre")
    private Genre genre;

    @NotNull(message = "Please select an album")
    private Long album;
}
