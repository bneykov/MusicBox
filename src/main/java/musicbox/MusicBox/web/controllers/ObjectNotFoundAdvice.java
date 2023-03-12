package musicbox.MusicBox.web.controllers;

import musicbox.MusicBox.utils.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ObjectNotFoundAdvice {
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ObjectNotFoundException.class)
    public String onObjectNotFound(ObjectNotFoundException onfe, Model model){
        model.addAttribute("id", onfe.getId());
        model.addAttribute("type", onfe.getType());
        return "object-not-found";
    }

}
