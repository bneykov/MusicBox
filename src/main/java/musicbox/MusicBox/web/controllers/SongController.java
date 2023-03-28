package musicbox.MusicBox.web.controllers;

import jakarta.validation.Valid;
import musicbox.MusicBox.model.dto.SongDTO;
import musicbox.MusicBox.model.enums.Genre;
import musicbox.MusicBox.services.album.AlbumService;
import musicbox.MusicBox.services.artist.ArtistService;
import musicbox.MusicBox.services.cloudinary.CloudinaryService;
import musicbox.MusicBox.services.song.SongService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Map;


@Controller
@RequestMapping("/songs")
public class SongController {
    private final SongService songService;
    private final AlbumService albumService;
    private final ArtistService artistService;
    private final CloudinaryService cloudinaryService;

    public SongController(SongService songService, AlbumService albumService, ArtistService artistService, CloudinaryService cloudinaryService) {
        this.songService = songService;
        this.albumService = albumService;
        this.artistService = artistService;
        this.cloudinaryService = cloudinaryService;
    }

    @ModelAttribute("songDTO")
    private SongDTO songDTO() {
        return new SongDTO();
    }

    @GetMapping("/add")
    public String addSong(Model model) {
        model.addAttribute("genres", Genre.values());
        model.addAttribute("albums", this.albumService.getAlbums());
        model.addAttribute("artists", this.artistService.getArtists());

        return "add-song";
    }

    @GetMapping("/all")
    public String allSongs(Model model) {
        model.addAttribute("title", "Songs");
        model.addAttribute("songs", this.songService.getSongs());


        return "view-all";
    }

    @PostMapping("/add")
    private String addSong(@Valid SongDTO songDTO, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("songDTO", songDTO);
            redirectAttributes
                    .addFlashAttribute(
                            "org.springframework.validation.BindingResult.songDTO", bindingResult);
            return "redirect:/songs/add";
        }

        Map<String, String> imageUploadResponse = this.cloudinaryService.uploadImage(songDTO.getImage());
        songDTO.setImageUrl(imageUploadResponse.get("secure_url"));
        songDTO.setImageUUID(imageUploadResponse.get("public_id"));
        this.songService.addSong(songDTO);
        return "redirect:/home";

    }

    @DeleteMapping("/remove/{id}")
    private String removeSong(@PathVariable Long id) {
        this.songService.removeSongConnections(id);
        this.songService.removeSong(id);
        return "redirect:/songs/all";

    }
}
