package musicbox.MusicBox.web.controllers;

import musicbox.MusicBox.model.entity.Playlist;
import musicbox.MusicBox.services.init.InitService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PlaylistControllerIT {
    @Autowired
    private MockMvc mockMvc;


    private Playlist playlist;

    @Autowired
    private InitService initService;

    @BeforeEach
    void setUp() {
        this.initService.init();
        this.playlist = this.initService.getPlaylist();
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
    @WithUserDetails("admin")
    void testAddPlaylistSuccess() throws Exception {
        mockMvc.perform(post("/playlists/add")
                        .param("name", "validName")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/playlists/all"));

    }
    @Test
    @WithUserDetails("admin")
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
    @WithUserDetails("admin")
    void testAuthenticatedGetAllPlaylists() throws Exception {
        mockMvc.perform(get("/playlists/all"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("playlists", hasSize(initService.getPlaylists().size())))
                .andExpect(view().name("all-playlists"));
    }

}
