package musicbox.MusicBox.utils.errors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidTokenException extends RuntimeException {



    public InvalidTokenException() {
        super("This link has expired");
    }


}
