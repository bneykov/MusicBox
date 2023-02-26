package musicbox.MusicBox.web.controllers;

import jakarta.validation.Valid;
import musicbox.MusicBox.model.dto.UserLoginDTO;
import musicbox.MusicBox.model.dto.UserRegisterDTO;
import musicbox.MusicBox.services.user.UserService;
import musicbox.MusicBox.utils.LoggedUser;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
    private final UserService userService;

    private final LoggedUser loggedUser;

    public UserController(UserService userService, LoggedUser loggedUser) {
        this.userService = userService;
        this.loggedUser = loggedUser;
    }

    @GetMapping("/login")
    public String login() {
        if (this.loggedUser.isLogged()) {
            return "redirect:/";
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        if (!this.loggedUser.isLogged()) {
            return "redirect:/login";
        }
        userService.logout();
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(@Valid UserLoginDTO loginDTO,BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("loginDTO", loginDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.loginDTO", result);

            return "redirect:/users/login";
        }

        boolean validCredentials = this.userService.login(loginDTO);

        if (!validCredentials) {
            redirectAttributes
                    .addFlashAttribute("loginDTO", loginDTO)
                    .addFlashAttribute("validCredentials", false);
            return "redirect:/users/login";
        }

        return "redirect:/home";
    }


    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid UserRegisterDTO registerDTO,
                           BindingResult bindingresult, RedirectAttributes redirectAttributes) {
        if (bindingresult.hasErrors()){
            redirectAttributes.addFlashAttribute("registerDTO", registerDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerDTO"
                    , bindingresult);
            return "redirect:/register";
        }
        userService.register(registerDTO);
        return "redirect:/login";
    }


    @ModelAttribute
    public UserLoginDTO loginDTO() {
        return new UserLoginDTO();
    }

    @ModelAttribute
    public UserRegisterDTO registerDTO() {
        return new UserRegisterDTO();
    }
}
