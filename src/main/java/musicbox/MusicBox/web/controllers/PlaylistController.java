package musicbox.MusicBox.web.controllers;

import jakarta.validation.Valid;
import musicbox.MusicBox.model.dto.PlaylistDTO;
import musicbox.MusicBox.model.entity.Playlist;
import musicbox.MusicBox.services.cloudinary.CloudinaryService;
import musicbox.MusicBox.services.playlist.PlaylistService;
import musicbox.MusicBox.services.user.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public PlaylistController(PlaylistService playlistService, CloudinaryService cloudinaryService) {
        this.playlistService = playlistService;
        this.cloudinaryService = cloudinaryService;
    }

    @ModelAttribute("playlistDTO")
    public PlaylistDTO playlistDTO() {
        return new PlaylistDTO();
    }

    @GetMapping("/add")
    public String addPlaylist() {

        return "create-playlist";
    }

    @GetMapping("/{id}")
    private String viewPlaylist(@PathVariable Long id, Model model
            , @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Playlist playlist = this.playlistService.getPlaylistById(id);
        if (!this.playlistService.isOwner(customUserDetails.getUsername(), id)) {
            throw new AccessDeniedException("You can't view this playlist");
        }
        model.addAttribute("songs", playlist.getSongs());
        model.addAttribute("currentPlaylist", playlist);
        return "playlist";
    }
    @GetMapping("/all")
    public String viewAllPlaylists(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.addAttribute("songId", null);
        model.addAttribute("currentUser", userDetails);
        model.addAttribute("playlists", this.playlistService.getUserPlaylists(userDetails.getId()));
        return "all-playlists";
    }

    @GetMapping("/add/{id}")
    public String addSongToPlaylist(Model model, @AuthenticationPrincipal CustomUserDetails userDetails,
                                    @PathVariable Long id) {
        model.addAttribute("songId", id);
        model.addAttribute("currentUser", userDetails);
        model.addAttribute("playlists", this.playlistService.getUserPlaylists(userDetails.getId()));
        return "all-playlists";
    }

    @PostMapping("/add")
    private String addPlaylist(@Valid PlaylistDTO playlistDTO, BindingResult bindingResult,
                               RedirectAttributes redirectAttributes, @AuthenticationPrincipal
                               CustomUserDetails userDetails) throws IOException {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("playlistDTO", playlistDTO);
            redirectAttributes
                    .addFlashAttribute(
                            "org.springframework.validation.BindingResult.playlistDTO", bindingResult);
            return "redirect:/playlists/add";
        }

        Map<String, String> imageUploadResponse = this.cloudinaryService.uploadImage(playlistDTO.getImage());
        playlistDTO.setImageUrl(imageUploadResponse.get("secure_url"));
        playlistDTO.setImageUUID(imageUploadResponse.get("public_id"));
        this.playlistService.addPlaylist(playlistDTO, userDetails);
        return "redirect:/playlists/all";

    }

    @PostMapping("/{playlistId}/add/{songId}")
    private String addSongToPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        this.playlistService.addSongToPlaylist(playlistId, songId);
        return "redirect:/playlists/" + playlistId;
    }


    @DeleteMapping("/{playlistId}/remove/{songId}")
    private String removeSongFromPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        this.playlistService.removeSongFromPlaylist(playlistId, songId);
        return "redirect:/playlists/" + playlistId;
    }

    @DeleteMapping("/remove/{id}")
    private String removePlaylist(@PathVariable Long id) {
        this.playlistService.removePlaylist(id);
        return "redirect:/playlists/all";
    }

}
