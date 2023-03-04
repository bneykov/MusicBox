package musicbox.MusicBox.web.controllers;

import jakarta.validation.Valid;
import musicbox.MusicBox.model.dto.UserLoginDTO;
import musicbox.MusicBox.model.dto.UserRegisterDTO;
import musicbox.MusicBox.services.user.UserService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;

    }
    @ModelAttribute
    public UserLoginDTO loginDTO() {
        return new UserLoginDTO();
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
    public String all() {

        return "/";
    }


    @GetMapping("/register")
    public String register() {
        return "register";
    }
    @PostMapping("/register")
    public String doRegister(@Valid UserRegisterDTO registerDTO, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.registerDTO",
                    bindingResult);
            redirectAttributes.addFlashAttribute("registerDTO", registerDTO);
            return "redirect:/users/register";
        }
        this.userService.register(registerDTO);
        return "redirect:/users/login";
    }






}
