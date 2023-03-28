package musicbox.MusicBox.web.controllers;

import musicbox.MusicBox.model.entity.Song;
import musicbox.MusicBox.services.init.InitService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SongControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private Song song;

    @Autowired
    private InitService initService;

    @BeforeEach
    void setUp() {
        this.initService.init();
        song = this.initService.getSong();

    }

    @AfterEach
    void tearDown(){
        initService.clearDatabase();
    }

    @Test
    @WithMockUser(
            roles = {"ADMIN", "USER"}
    )
    void testGetAddSongPageShown() throws Exception {
        mockMvc.perform(get("/songs/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-song"));
    }

    @Test
    @WithMockUser
    void testGetAddSongPageForbidden() throws Exception {
        mockMvc.perform(get("/songs/add"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(
            roles = {"ADMIN", "USER"}
    )
    void testAdminAddSongFail() throws Exception {

        mockMvc.perform(multipart("/songs/add")
                        .param("name", "validSongName")
                        .param("album", "") //album cannot be null
                        .param("duration", "300")
                        .param("genre", "POP")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeHasErrors())
                .andExpect(flash().attributeExists("songDTO"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.songDTO"))
                .andExpect(redirectedUrl("/songs/add"));
    }
    @Test
    @Order(1)
    @WithMockUser(
            roles = {"ADMIN", "USER"}
    )
    void testAdminAddSongSuccess() throws Exception {

        mockMvc.perform(multipart("/songs/add")
                        .param("name", "validSongName")
                        .param("album", song.getAlbum().getId().toString())
                        .param("duration", "300")
                        .param("genre", "POP")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }
    @Test
    @WithMockUser
    void testNonAdminAddSongForbidden() throws Exception {

        mockMvc.perform(multipart("/songs/add")
                        .param("name", "validSongName")
                        .param("album", "1")
                        .param("duration", "300")
                        .param("genre", "POP")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().isForbidden());

    }

    @Test
    void testAnonymousGetAllSongs() throws Exception {
        mockMvc.perform(get("/songs/all"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("title", "Songs"))
                .andExpect(model().attribute("songs", hasSize(initService.getSongs().size())))
                .andExpect(view().name("view-all"));
    }
    @Test
    @WithMockUser
    void testAuthenticatedGetAllSongs() throws Exception {
        mockMvc.perform(get("/songs/all"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("title", "Songs"))
                .andExpect(model().attribute("songs", hasSize(initService.getSongs().size())))
                .andExpect(view().name("view-all"));
    }

    @Test
    @WithMockUser(
            roles = {"ADMIN", "USER"}
    )
    void testAdminRemoveSong() throws Exception {
        mockMvc.perform(delete("/songs/remove/{id}", song.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/songs/all"));
    }

    @Test
    @WithMockUser
    void testNonAdminRemoveSongForbidden() throws Exception {
        mockMvc.perform(delete("/albums/remove/{id}", song.getId())
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }
}
