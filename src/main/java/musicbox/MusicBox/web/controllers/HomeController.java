package musicbox.MusicBox.web.controllers;

import musicbox.MusicBox.services.album.AlbumService;
import musicbox.MusicBox.services.artist.ArtistService;
import musicbox.MusicBox.services.song.SongService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final SongService songService;
    private final AlbumService albumService;
    private final ArtistService artistService;

    public HomeController(SongService songService, AlbumService albumService, ArtistService artistService) {
        this.songService = songService;
        this.albumService = albumService;
        this.artistService = artistService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("songs", this.songService.getHomeSongs());
        model.addAttribute("artists", this.artistService.getHomeArtists());
        model.addAttribute("albums", this.albumService.getHomeAlbums());

        return "home";
    }
}
