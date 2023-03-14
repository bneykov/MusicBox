package musicbox.MusicBox.web.controllers;

import jakarta.validation.Valid;
import musicbox.MusicBox.model.dto.AlbumDTO;
import musicbox.MusicBox.services.album.AlbumService;
import musicbox.MusicBox.services.artist.ArtistService;
import musicbox.MusicBox.services.cloudinary.CloudinaryService;
import musicbox.MusicBox.utils.ObjectNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
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
    private AlbumDTO albumDTO() {
        return new AlbumDTO();
    }

    @GetMapping("/add")
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
    @GetMapping("/{id}")
    public String album(Model model, @PathVariable Long id) {
        if (this.albumService.getAlbumById(id) == null) {
            throw new ObjectNotFoundException(id, "Album");
        }
        model.addAttribute("title", "Songs");
        model.addAttribute("songs", this.albumService.getSongsByAlbumId(id));

        return "view-all";
    }
    @GetMapping("/remove/{id}")
    private String removeAlbum(@PathVariable Long id){
        if (this.albumService.getAlbumById(id) == null) {
            throw new ObjectNotFoundException(id, "Album");
        }
        this.albumService.removeAlbum(id);
        return "redirect:/albums/all";
    }
    @PostMapping("/add")
    private String addAlbum(@Valid AlbumDTO albumDTO, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("albumDTO", albumDTO);
            redirectAttributes
                    .addFlashAttribute(
                            "org.springframework.validation.BindingResult.albumDTO", bindingResult);
            return "redirect:/albums/add";
        }
        if (!albumDTO.getImage().isEmpty()) {
            File image = File.createTempFile("temp", null);
            albumDTO.getImage().transferTo(image);
            Map<String, String> imageUploadResponse = this.cloudinaryService.uploadImage(image);
            albumDTO.setImageUrl(imageUploadResponse.get("secure_url"));
            albumDTO.setImageUUID(imageUploadResponse.get("public"));
        }
        this.albumService.addAlbum(albumDTO);
        return "redirect:/home";

    }

}
