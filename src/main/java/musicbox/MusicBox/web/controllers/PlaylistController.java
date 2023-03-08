package musicbox.MusicBox.web.controllers;

import jakarta.validation.Valid;
import musicbox.MusicBox.model.dto.PlaylistDTO;
import musicbox.MusicBox.model.entity.Playlist;
import musicbox.MusicBox.model.entity.Song;
import musicbox.MusicBox.model.entity.UserEntity;
import musicbox.MusicBox.services.playlist.PlaylistService;
import musicbox.MusicBox.services.user.UserDetailsServiceImpl;
import musicbox.MusicBox.services.user.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;
    private final UserService userService;

    public PlaylistController(PlaylistService playlistService, UserService userService) {
        this.playlistService = playlistService;
        this.userService = userService;
    }

    @ModelAttribute("playlistDTO")
    public PlaylistDTO playlistDTO(){
        return new PlaylistDTO();
    }

    @GetMapping("/add")
    public String addPlaylist() {

        return "create-playlist";
    } @GetMapping("/all")
    public String viewPlaylists(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = this.userService.findByUsername(userDetails.getUsername());
        model.addAttribute("songId", null);
        model.addAttribute("currentUser", user);
        model.addAttribute("playlists", this.playlistService.getUserPlaylists(user.getId()));
        return "all-playlists";
    }
    @GetMapping("/add/{id}")
    public String addSongToPlaylist(Model model, @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        UserEntity user = this.userService.findByUsername(userDetails.getUsername());
        model.addAttribute("songId", id);
        model.addAttribute("currentUser", user);
        model.addAttribute("playlists", this.playlistService.getUserPlaylists(user.getId()));
        return "all-playlists";
    }
    @PostMapping("/add")
    private String addSong(@Valid PlaylistDTO playlistDTO, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes, @AuthenticationPrincipal
                           UserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("playlistDTO", playlistDTO);
            redirectAttributes
                    .addFlashAttribute(
                            "org.springframework.validation.BindingResult.playlistDTO", bindingResult);
            return "redirect:/playlists/add";
        }

        this.playlistService.addPlaylist(playlistDTO, userDetails);

        return "redirect:/playlists/all";

    }
    @GetMapping("/{playlistId}/add/{songId}")
    private String addSongToPlaylist(@PathVariable Long playlistId, @PathVariable Long songId){
        this.playlistService.addSongToPlaylist(playlistId, songId);
        return "redirect:/songs/all";
    }
    @GetMapping("/{playlistId}/remove/{songId}")
    private String removeSongFromPlaylist(@PathVariable Long playlistId, @PathVariable Long songId){
        this.playlistService.removeSongFromPlaylist(playlistId, songId);
        return "redirect:/playlists/" + playlistId;
    }
 @GetMapping("/remove/{id}")
    private String removePlaylist(@PathVariable Long id){
       this.playlistService.removePlaylist(id);
        return "redirect:/playlists/all";
    }

    @GetMapping("/{id}")
    private String viewPlaylist(@PathVariable Long id, Model model){
        Playlist playlist = this.playlistService.getById(id);
        model.addAttribute("songs", playlist.getSongs());
        model.addAttribute("playlistId", id);
        return "playlist";
    }
}
