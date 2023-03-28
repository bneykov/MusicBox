package musicbox.MusicBox.web.controllers;

import jakarta.validation.Valid;
import musicbox.MusicBox.model.dto.UserUpdateDTO;
import musicbox.MusicBox.services.user.CustomUserDetails;
import musicbox.MusicBox.services.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    public ProfileController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @ModelAttribute(name = "updateDTO")
    public UserUpdateDTO updateDTO() {
        return this.modelMapper.map(this.userService.getCurrentUser(), UserUpdateDTO.class);
    }


    @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal CustomUserDetails userDetails){
        model.addAttribute("currentUser", userDetails);
        return "profile";
    }
    @PutMapping("/profile/{id}/edit")
    public String editProfile(@PathVariable Long id, @Valid UserUpdateDTO updateDTO, BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.updateDTO",
                    bindingResult);
            redirectAttributes.addFlashAttribute("updateDTO", updateDTO);
            return "redirect:/profile";
        }
            this.userService.update(updateDTO, id);
            return "redirect:/profile";
    }
}
