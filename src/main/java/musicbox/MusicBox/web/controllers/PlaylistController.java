package musicbox.MusicBox.web.controllers;

import jakarta.validation.Valid;
import musicbox.MusicBox.model.CustomUserDetails;
import musicbox.MusicBox.model.dto.PlaylistDTO;
import musicbox.MusicBox.model.entity.Playlist;
import musicbox.MusicBox.services.playlist.PlaylistService;
import musicbox.MusicBox.services.song.SongService;
import musicbox.MusicBox.utils.ObjectNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Controller
@RequestMapping("/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;
    private final SongService songService;

    public PlaylistController(PlaylistService playlistService, SongService songService) {
        this.playlistService = playlistService;
        this.songService = songService;
    }

    @ModelAttribute("playlistDTO")
    public PlaylistDTO playlistDTO() {
        return new PlaylistDTO();
    }

    @GetMapping("/add")
    public String addPlaylist() {

        return "create-playlist";
    }

    @GetMapping("/all")
    public String viewPlaylists(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.addAttribute("songId", null);
        model.addAttribute("currentUser", userDetails);
        model.addAttribute("playlists", this.playlistService.getUserPlaylists(userDetails.getId()));
        return "all-playlists";
    }

    @GetMapping("/add/{id}")
    public String addSongToPlaylist(Model model, @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id) {
        if (this.songService.getSongById(id) == null) {
            throw new ObjectNotFoundException(id, "Song");
        }
        model.addAttribute("songId", id);
        model.addAttribute("currentUser", userDetails);
        model.addAttribute("playlists", this.playlistService.getUserPlaylists(userDetails.getId()));
        return "all-playlists";
    }

    @PostMapping("/add")
    private String addSong(@Valid PlaylistDTO playlistDTO, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes, @AuthenticationPrincipal
                           CustomUserDetails userDetails) {
        if (!this.playlistService.addPlaylist(playlistDTO, userDetails)) {
            bindingResult.addError(new FieldError("playlistDTO", "name",
                    "You already have a playlist with this name"));
        }
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("playlistDTO", playlistDTO);
            redirectAttributes
                    .addFlashAttribute(
                            "org.springframework.validation.BindingResult.playlistDTO", bindingResult);
            return "redirect:/playlists/add";
        }

        return "redirect:/playlists/all";

    }

    @GetMapping("/{playlistId}/add/{songId}")

    private String addSongToPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        if (this.songService.getSongById(songId) == null) {
            throw new ObjectNotFoundException(songId, "Song");
        } else if (this.playlistService.getPlaylistById(playlistId) == null) {
            throw new ObjectNotFoundException(playlistId, "Playlist");
        }

        this.playlistService.addSongToPlaylist(playlistId, songId);
        return "redirect:/playlists/" + playlistId;
    }

    @GetMapping("/{playlistId}/remove/{songId}")
    private String removeSongFromPlaylist(@PathVariable Long playlistId, @PathVariable Long songId,
    @AuthenticationPrincipal CustomUserDetails userDetails) {
        Playlist playlist = this.playlistService.getPlaylistById(playlistId);
        if (this.songService.getSongById(songId) == null) {
            throw new ObjectNotFoundException(songId, "Song");

        } else if (playlist == null) {
            throw new ObjectNotFoundException(playlistId, "Playlist");

        } else if (!Objects.equals(playlist.getUserEntity().getId(), userDetails.getId())) {
            throw new AccessDeniedException("Sorry, you can't edit this playlist");

        }
        this.playlistService.removeSongFromPlaylist(playlistId, songId);
        return "redirect:/playlists/" + playlistId;
    }

    @GetMapping("/remove/{id}")
    private String removePlaylist(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Playlist playlist = this.playlistService.getPlaylistById(id);
        if (playlist == null) {
            throw new ObjectNotFoundException(id, "Playlist");
        }

        this.playlistService.removePlaylist(id);
        return "redirect:/playlists/all";
    }

    @GetMapping("/{id}")
    private String viewPlaylist(@PathVariable Long id, Model model) {
        Playlist playlist = this.playlistService.getPlaylistById(id);
        if (playlist == null) {
            throw new ObjectNotFoundException(id, "Playlist");
        }
        model.addAttribute("songs", playlist.getSongs());
        model.addAttribute("currentPlaylist", playlist);
        return "playlist";
    }
}
