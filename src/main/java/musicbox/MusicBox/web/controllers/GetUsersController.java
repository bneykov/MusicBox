package musicbox.MusicBox.web.controllers;

import musicbox.MusicBox.model.dto.UserViewDTO;
import musicbox.MusicBox.services.user.UserService;
import musicbox.MusicBox.utils.ObjectNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")

public class GetUsersController {
    private final UserService userService;

    public GetUsersController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/get")
    public ResponseEntity<List<UserViewDTO>> getUsers() {
        List<UserViewDTO> users = userService.mapUsers(userService.getUsers());
        return ResponseEntity.ok(users);
    }


}
