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
public class AlbumDTO {
    @NotNull
    private String name;
    private String cover;
    @NotNull
    private Long artist;
}
