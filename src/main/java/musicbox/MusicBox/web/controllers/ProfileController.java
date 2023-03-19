package musicbox.MusicBox.web.controllers;

import musicbox.MusicBox.services.user.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {


    @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal CustomUserDetails userDetails){
        model.addAttribute("currentUser", userDetails);
        return "profile";
    }
}
