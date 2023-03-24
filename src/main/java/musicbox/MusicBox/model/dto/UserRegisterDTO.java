package musicbox.MusicBox.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import musicbox.MusicBox.utils.validation.annotation.PasswordMatch;
import musicbox.MusicBox.utils.validation.annotation.UniqueEmail;
import musicbox.MusicBox.utils.validation.annotation.UniqueUsername;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatch(password = "password", confirmPassword = "confirmPassword")
public class UserRegisterDTO {
    @NotNull
    @UniqueUsername
    @Size(min = 2, max = 20, message = "Username length must be between 2 and 20 characters long")
    private String username;
    @NotNull
    @Size(min = 2, max = 30, message = "Name length must be between 2 and 30 characters long")
    private String name;
    @Email(message = "Enter a valid email address")
    @UniqueEmail
    @NotNull
    @NotEmpty(message = "Enter a valid email address")
    private String email;
    @NotNull
    @Size(min = 7, message = "Password length must be at least 7 characters long")
    private String password;
    private String confirmPassword;
    private String imageUrl;


}
