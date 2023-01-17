package musicbox.MusicBox.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SongDTO {

    @NotNull
    private String title;

    private String lyrics;

    @NotNull
    private String path;

    @NotNull
    private String genre;

    @NotNull
    private Long album;
}
