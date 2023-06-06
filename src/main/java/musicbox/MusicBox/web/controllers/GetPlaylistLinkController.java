package musicbox.MusicBox.web.controllers;

import musicbox.MusicBox.services.playlist.PlaylistService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetPlaylistLinkController {
    private final PlaylistService playlistService;

    public GetPlaylistLinkController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @PreAuthorize("isOwner(#id)")
    @GetMapping("/playlists/{id}/create-link")
    public ResponseEntity<String> getLink(@PathVariable Long id) {
        String link = this.playlistService.createShareableLink(id);
        return ResponseEntity.ok(link);
    }
}
