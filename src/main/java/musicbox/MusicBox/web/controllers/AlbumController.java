package musicbox.MusicBox.web.controllers;

import jakarta.validation.Valid;
import musicbox.MusicBox.model.dto.AlbumDTO;
import musicbox.MusicBox.model.entity.Album;
import musicbox.MusicBox.model.entity.Artist;
import musicbox.MusicBox.services.album.AlbumService;
import musicbox.MusicBox.services.artist.ArtistService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/albums")
public class AlbumController {
    private final AlbumService albumService;
    private final ArtistService artistService;

    public AlbumController(AlbumService albumService, ArtistService artistService) {
        this.albumService = albumService;
        this.artistService = artistService;
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
    @GetMapping("/remove/{id}")
    private String removeAlbum(@PathVariable Long id){
        this.albumService.removeAlbum(id);
        return "redirect:/albums/all";
    }
    @PostMapping("/add")
    private String addAlbum(@Valid AlbumDTO albumDTO, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("albumDTO", albumDTO);
            redirectAttributes
                    .addFlashAttribute(
                            "org.springframework.validation.BindingResult.albumDTO", bindingResult);
            return "redirect:/albums/add";
        }
        this.albumService.addAlbum(albumDTO);
        return "redirect:/home";

    }

}
