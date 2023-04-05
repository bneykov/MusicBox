package musicbox.MusicBox.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import musicbox.MusicBox.utils.validation.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatch(password = "newPassword", confirmPassword = "confirmPassword")
public class UserUpdateDTO {
    @NotNull
    @UniqueUsernameIfNew
    @Size(min = 2, max = 20, message = "Username length must be between 2 and 20 characters long")
    private String username;

    @ValidImageFormat
    private MultipartFile image;
    @NotNull
    @Size(min = 2, max = 50, message = "Name length must be between 2 and 30 characters long")
    private String name;
    @Email(message = "Enter a valid email address")
    @UniqueEmailIfNew
    @NotNull
    @NotEmpty(message = "Enter a valid email address")
    private String email;

    @ValidCurrentPassword
    private String currentPassword;
    @CustomSize
    private String newPassword;
    private String confirmPassword;
    private String imageUrl;

    private String imageUUID;


}
