package musicbox.MusicBox.web.controllers;

import musicbox.MusicBox.model.dto.UserLoginDTO;
import musicbox.MusicBox.model.dto.UserRegisterDTO;
import musicbox.MusicBox.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(){
        return "auth-login";
    }

    @GetMapping("/logout")
    public String logout(){
        userService.logout();
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(UserLoginDTO userLoginDTO){
        userService.login(userLoginDTO);
        System.out.println("User is logged: " + userService.login(userLoginDTO));
        return "redirect:/";
    }

    @GetMapping("/register")
    public String register(){
        return "auth-register";
    }

    @PostMapping("/register")
    public String register(UserRegisterDTO userRegisterDTO){

        return "redirect:/";
    }
}
