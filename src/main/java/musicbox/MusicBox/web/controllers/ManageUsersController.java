package musicbox.MusicBox.web.controllers;

import musicbox.MusicBox.model.dto.UserViewDTO;
import musicbox.MusicBox.services.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@RequestMapping("/users")

public class ManageUsersController {
    private final UserService userService;

    public ManageUsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserViewDTO>> getUsers() {
        List<UserViewDTO> users = userService.mapUsers(userService.getUsers());
        return ResponseEntity.ok(users);
    }
    @GetMapping("/{id}/change_role")
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView changeRole(@PathVariable Long id) {
        this.userService.changeRole(id);
        return new RedirectView("/users/all");
    }

}
