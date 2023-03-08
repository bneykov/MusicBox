package musicbox.MusicBox.web.controllers;

import jakarta.validation.Valid;
import musicbox.MusicBox.model.dto.SongDTO;
import musicbox.MusicBox.model.entity.Song;
import musicbox.MusicBox.model.enums.Genre;
import musicbox.MusicBox.services.album.AlbumService;
import musicbox.MusicBox.services.artist.ArtistService;
import musicbox.MusicBox.services.song.SongService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
@RequestMapping("/songs")
public class SongController {
    private final SongService songService;
    private final AlbumService albumService;
    private final ArtistService artistService;

    public SongController(SongService songService, AlbumService albumService, ArtistService artistService) {
        this.songService = songService;
        this.albumService = albumService;
        this.artistService = artistService;
    }

    @ModelAttribute("songDTO")
    private SongDTO songDTO() {
        return new SongDTO();
    }
    @GetMapping("/add")
    public String addSong(Model model) {
        model.addAttribute("genres", Genre.values());
        model.addAttribute("albums",this.albumService.getAlbums());
        model.addAttribute("artists",this.artistService.getArtists());

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
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("songDTO", songDTO);
            redirectAttributes
                    .addFlashAttribute(
                            "org.springframework.validation.BindingResult.songDTO", bindingResult);
            return "redirect:/songs/add";
        }
        this.songService.addSong(songDTO);
        return "redirect:/home";

    }
    @GetMapping("/remove/{id}")
    private String removeSong(@PathVariable Long id) {
       this.songService.removeSong(id);
        return "redirect:/songs/all";

    }
}
