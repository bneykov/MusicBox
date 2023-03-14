package musicbox.MusicBox.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import musicbox.MusicBox.model.entity.UserRole;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class UserViewDTO {
    private Long id;
    private String username;
    private String name;
    private String modified;
    private String email;

    private List<UserRole> roles;
}
