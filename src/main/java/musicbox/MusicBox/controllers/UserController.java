package musicbox.MusicBox.controllers;

import musicbox.MusicBox.model.dto.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class UserController {
    @GetMapping("")
    public String register(){
        return "register";
    }

    @PostMapping("")
    public String createUser(UserDTO userDTO){
        System.out.println("Creating new user..." + userDTO);
        return "usercreated";
    }

}
