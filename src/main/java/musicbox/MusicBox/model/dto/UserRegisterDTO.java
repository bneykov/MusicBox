package musicbox.MusicBox.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {

    @NotEmpty
    @Size(min = 2, max = 30)
    private String name;
    @Email
    @NotEmpty
    private String email;
    @NotEmpty
    @Size(min = 6)
    private String password;
    private String confirmPassword;
    private String image;


}
