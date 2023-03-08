package musicbox.MusicBox.web.controllers;

import jakarta.validation.Valid;
import musicbox.MusicBox.model.dto.ArtistDTO;
import musicbox.MusicBox.model.entity.Artist;
import musicbox.MusicBox.services.artist.ArtistService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/artists")
public class ArtistController {
    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @ModelAttribute("artistDTO")
    private ArtistDTO artistDTO() {
        return new ArtistDTO();
    }

    @GetMapping("/add")
    public String addArtist() {

        return "add-artist";
    }

    @GetMapping("/all")
    public String allArtists(Model model) {
        model.addAttribute("title", "Artists");
        model.addAttribute("artists", this.artistService.getArtists());


        return "view-all";
    }
    @GetMapping("/remove/{id}")
    private String removeArtist(@PathVariable Long id){
        this.artistService.removeArtist(id);
        return "redirect:/artists/all";
    }

    @PostMapping("/add")
    private String addArtist(@Valid ArtistDTO artistDTO, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("artistDTO", artistDTO);
            redirectAttributes
                    .addFlashAttribute(
                            "org.springframework.validation.BindingResult.artistDTO", bindingResult);
            return "redirect:/artists/add";
        }
        this.artistService.addArtist(artistDTO);
        return "redirect:/home";

    }
}
