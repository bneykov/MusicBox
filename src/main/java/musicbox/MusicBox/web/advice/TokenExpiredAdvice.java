package musicbox.MusicBox.web.advice;

import musicbox.MusicBox.utils.errors.InvalidTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TokenExpiredAdvice {
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidTokenException.class)
    public String onInvalidToken() {

        return "invalid-token";
    }

}
