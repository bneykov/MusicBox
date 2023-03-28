package musicbox.MusicBox.web.controllers;

import jakarta.validation.Valid;
import musicbox.MusicBox.model.dto.UserRegisterDTO;
import musicbox.MusicBox.model.enums.RoleEnum;
import musicbox.MusicBox.services.user.CustomUserDetails;
import musicbox.MusicBox.services.user.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute(name = "registerDTO")
    public UserRegisterDTO registerDTO() {
        return new UserRegisterDTO();
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login-error")
    public String onFailedLogin(@ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                                String username,
                                RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("invalidCredentials", true);
        redirectAttributes.addFlashAttribute("username", username);
        return "redirect:/users/login";

    }

    @GetMapping("/all")
    public String all(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("roles", RoleEnum.values());
        model.addAttribute("users", this.userService.getUsers());
        return "all-users";
    }


    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid UserRegisterDTO registerDTO, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerDTO", bindingResult);
            redirectAttributes.addFlashAttribute("registerDTO", registerDTO);
            return "redirect:/users/register";
        }
        this.userService.register(registerDTO);
        return "redirect:/users/login";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/change_role")
    public String changeRole(@PathVariable Long id) {
        this.userService.changeRole(id);
        return "redirect:/users/all";
    }

}
