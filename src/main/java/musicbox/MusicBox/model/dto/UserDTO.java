package musicbox.MusicBox.model.dto;

import jakarta.validation.constraints.Email;
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
public class UserDTO {

    @NotNull
    @Size(min = 2)
    private String firstName;

    @Size(min = 2)
    @NotNull
    private String lastName;
    @NotNull
    @Email
    private String email;
    private String password;

    private String image;

    @Override
    public String toString() {
        return "UserDTO{" +
               "firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", email='" + email + '\'' +
               ", password='" + password + '\'' +
               '}';
    }
}
