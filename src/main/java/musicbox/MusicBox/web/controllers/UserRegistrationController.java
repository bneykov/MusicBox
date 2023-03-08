//package musicbox.MusicBox.web.controllers;
//
//import jakarta.validation.Valid;
//import musicbox.MusicBox.model.dto.UserRegisterDTO;
//import musicbox.MusicBox.services.user.UserService;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//@Controller
//public class UserRegistrationController {
//    private final UserService userService;
//
//    public UserRegistrationController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @ModelAttribute("userModel")
//    public void initUserModel(Model model){
//        model.addAttribute("userModel", new UserRegisterDTO());
//    }
//    @GetMapping("/register")
//    public String register() {
//        return "register";
//    }
//
//    @PostMapping("/register")
//    public String register(@Valid UserRegisterDTO userModel,
//                           BindingResult bindingresult, RedirectAttributes redirectAttributes) {
//        if (bindingresult.hasErrors()){
//            redirectAttributes.addFlashAttribute("userModel", userModel);
//            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userModel"
//            , bindingresult);
//            return "redirect:/register";
//        }
//        userService.register(userModel);
//        return "redirect:/login";
//    }
//}
