package musicbox.MusicBox.web.controllers;

import jakarta.validation.Valid;
import musicbox.MusicBox.model.dto.AlbumDTO;
import musicbox.MusicBox.services.album.AlbumService;
import musicbox.MusicBox.services.artist.ArtistService;
import musicbox.MusicBox.services.cloudinary.CloudinaryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/albums")
public class AlbumController {
    private final AlbumService albumService;
    private final ArtistService artistService;
    private final CloudinaryService cloudinaryService;

    public AlbumController(AlbumService albumService, ArtistService artistService, CloudinaryService cloudinaryService) {
        this.albumService = albumService;
        this.artistService = artistService;
        this.cloudinaryService = cloudinaryService;
    }

    @ModelAttribute("albumDTO")
    public AlbumDTO albumDTO() {
        return new AlbumDTO();
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addAlbum(Model model) {
        model.addAttribute("artists", this.artistService.getArtists());
        return "add-album";
    }

    @GetMapping("/all")
    public String allAlbums(Model model) {
        model.addAttribute("title", "Albums");
        model.addAttribute("albums", this.albumService.getAlbums());

        return "view-all";
    }

    @GetMapping("/view/{id}")
    public String album(Model model, @PathVariable Long id) {

        model.addAttribute("title", "Songs");
        model.addAttribute("songs", this.albumService.getSongsByAlbumId(id));

        return "view-all";
    }
    @DeleteMapping("/remove/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String removeAlbum(@PathVariable Long id) {
        this.albumService.removeAlbum(id);
        return "redirect:/albums/all";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addAlbum(@Valid AlbumDTO albumDTO, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("albumDTO", albumDTO);
            redirectAttributes
                    .addFlashAttribute(
                            "org.springframework.validation.BindingResult.albumDTO", bindingResult);
            return "redirect:/albums/add";
        }

        Map<String, String> imageUploadResponse = this.cloudinaryService.uploadImage(albumDTO().getImage());
        albumDTO.setImageUrl(imageUploadResponse.get("secure_url"));
        albumDTO.setImageUUID(imageUploadResponse.get("public_id"));
        this.albumService.addAlbum(albumDTO);
        return "redirect:/albums/all";

    }

}
