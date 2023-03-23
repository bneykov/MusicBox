package musicbox.MusicBox.web.controllers;

import jakarta.validation.Valid;
import musicbox.MusicBox.model.dto.ArtistDTO;
import musicbox.MusicBox.services.artist.ArtistService;
import musicbox.MusicBox.services.cloudinary.CloudinaryService;
import musicbox.MusicBox.utils.errors.ObjectNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/artists")
public class ArtistController {
    private final ArtistService artistService;
    private final CloudinaryService cloudinaryService;

    public ArtistController(ArtistService artistService, CloudinaryService cloudinaryService) {
        this.artistService = artistService;
        this.cloudinaryService = cloudinaryService;
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

    @GetMapping("/{id}")
    public String artist(Model model, @PathVariable Long id) {
        if (this.artistService.getArtistById(id) == null) {
            throw new ObjectNotFoundException(id, "Artist");
        }
        model.addAttribute("title", "Albums");
        model.addAttribute("albums", this.artistService.getAlbumsByArtistId(id));
        return "view-all";
    }

    @GetMapping("/remove/{id}")
    private String removeArtist(@PathVariable Long id) {
        if (this.artistService.getArtistById(id) == null) {
            throw new ObjectNotFoundException(id, "Artist");
        }
        this.artistService.removeArtist(id);
        return "redirect:/artists/all";
    }

    @PostMapping("/add")
    private String addArtist(@Valid ArtistDTO artistDTO, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("artistDTO", artistDTO);
            redirectAttributes
                    .addFlashAttribute(
                            "org.springframework.validation.BindingResult.artistDTO", bindingResult);
            return "redirect:/artists/add";
        }

        Map<String, String> imageUploadResponse = this.cloudinaryService.uploadImage(artistDTO.getImage());
        artistDTO.setImageUrl(imageUploadResponse.get("secure_url"));
        artistDTO.setImageUUID(imageUploadResponse.get("public_id"));
        this.artistService.addArtist(artistDTO);
        return "redirect:/home";

    }
}
