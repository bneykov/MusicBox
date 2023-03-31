package musicbox.MusicBox.web.controllers;

import musicbox.MusicBox.model.entity.Playlist;
import musicbox.MusicBox.model.entity.Song;
import musicbox.MusicBox.model.entity.UserEntity;
import musicbox.MusicBox.services.init.InitService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PlaylistControllerIT {
    @Autowired
    private MockMvc mockMvc;


    private Playlist playlist;

    private Song song;

    private UserEntity admin;

    @Autowired
    private InitService initService;

    @BeforeEach
    void setUp() {
        this.initService.init();
        this.song = this.initService.getSong();
        this.playlist = this.initService.getPlaylist();
        this.admin = this.initService.getAdmin();
    }

    @AfterEach
    void tearDown() {
        this.initService.clearDatabase();
    }

    @Test
    @WithMockUser
    void testAddPlaylistPageShown() throws Exception {
        mockMvc.perform(get("/playlists/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-playlist"));
    }

    @Test
    void testAddPlaylistPageNeedsAuthenticatedUser() throws Exception {
        mockMvc.perform(get("/playlists/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));

    }

    @Test
    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testAddPlaylistSuccess() throws Exception {
        mockMvc.perform(post("/playlists/add")
                        .param("name", "validName")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/playlists/all"));

    }

    @Test
    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testAddPlaylistFail() throws Exception {
        mockMvc.perform(post("/playlists/add")
                        .param("name", "1") //invalid name
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("playlistDTO"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.playlistDTO"))
                .andExpect(model().attributeHasErrors())
                .andExpect(redirectedUrl("/playlists/add"));

    }

    @Test
    void testAnonymousGetAllPlaylistsNeedsAuthentication() throws Exception {
        mockMvc.perform(get("/playlists/all"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));

    }

    @Test
    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testAuthenticatedAllPlaylistsPageShown() throws Exception {
        mockMvc.perform(get("/playlists/all"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("playlists", hasSize(initService.getPlaylists().size())))
                .andExpect(model().attribute("currentUser", hasProperty("id",
                        is(admin.getId()))
                ))
                .andExpect(view().name("all-playlists"));
    }

    @Test
    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testOwnerViewPlaylistSuccess() throws Exception {

        mockMvc.perform(get("/playlists/{id}", playlist.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("songs", hasSize(playlist.getSongs().size())))
                .andExpect(model().attribute("currentPlaylist",
                        hasProperty("id", is(playlist.getId()))))
                .andExpect(view().name("playlist"));
    }

    @Test
    @WithMockUser
    void testNonOwnerViewPlaylistForbidden() throws Exception {

        mockMvc.perform(get("/playlists/{id}", playlist.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testAddSongToPlaylistPageShown() throws Exception {
        mockMvc.perform(get("/playlists/add/{id}", song.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("songId", song.getId()))
                .andExpect(model().attribute("currentUser", hasProperty("id",
                        is(admin.getId()))
                ))
                .andExpect(model().attribute("playlists", hasSize(initService.getPlaylists().size())))
                .andExpect(view().name("all-playlists"));
    }

    @Test
    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testOwnerAddSongToPlaylistSuccess() throws Exception {
        mockMvc.perform(post("/playlists/{playlistId}/add/{songId}", playlist.getId(), song.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/playlists/" + playlist.getId()));
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testNonOwnerAddSongToPlaylistForbidden() throws Exception {
        mockMvc.perform(post("/playlists/{playlistId}/add/{songId}", playlist.getId(), song.getId())
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testOwnerDeletePlaylistSuccess() throws Exception {
        mockMvc.perform(delete("/playlists/remove/{playlistId}", playlist.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/playlists/all"));
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testNonOwnerDeletePlaylistForbidden() throws Exception {
        mockMvc.perform(delete("/playlists/remove/{playlistId}", playlist.getId())
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testOwnerRemoveSongFromPlaylistSuccess() throws Exception {
        mockMvc.perform(delete("/playlists/{playlistId}/remove/{songId}", playlist.getId(), song.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/playlists/" + playlist.getId()));
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testNonOwnerRemoveSongFromPlaylistForbidden() throws Exception {
        mockMvc.perform(delete("/playlists/{playlistId}/remove/{songId}", playlist.getId(), song.getId())
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

}
