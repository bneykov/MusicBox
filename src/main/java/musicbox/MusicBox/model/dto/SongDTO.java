package musicbox.MusicBox.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import musicbox.MusicBox.model.entity.Artist;
import musicbox.MusicBox.model.enums.Genre;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SongDTO {

    @NotNull
    private String title;
    @Positive
    private int length;
    @NotNull
    private Set<Long> artists;

    private String path;

    @NotNull
    private Genre genre;

    @NotNull
    private Long album;
}
