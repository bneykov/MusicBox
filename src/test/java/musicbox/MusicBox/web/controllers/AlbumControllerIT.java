package musicbox.MusicBox.web.controllers;

import musicbox.MusicBox.model.entity.Album;
import musicbox.MusicBox.model.entity.Song;
import musicbox.MusicBox.services.init.InitService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AlbumControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private InitService initService;

    private Album album;


    @BeforeEach
    void setUp() {
        this.initService.init();
        this.album = this.initService.getAlbum();
    }

    @AfterEach
    void tearDown() {
        this.initService.clearDatabase();
    }

    @Test
    @WithMockUser(
            roles = {"ADMIN", "USER"}
    )
    void testGetAddAlbumPageShown() throws Exception {
        mockMvc.perform(get("/albums/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-album"));
    }

    @Test
    @WithMockUser
    void testGetAddAlbumPageForbidden() throws Exception {
        mockMvc.perform(get("/albums/add"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(
            roles = {"ADMIN", "USER"}
    )
    void testAdminAddAlbumFail() throws Exception {

        mockMvc.perform(multipart("/albums/add")
                        .param("name", "validArtistName")
                        .param("artists", "") //cannot be empty
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeHasErrors())
                .andExpect(flash().attributeExists("albumDTO"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.albumDTO"))
                .andExpect(redirectedUrl("/albums/add"));
    }

    @Test
    @WithMockUser(
            roles = {"ADMIN", "USER"}
    )
    void testAdminAddAlbumSuccess() throws Exception {

        mockMvc.perform(multipart("/albums/add")
                        .param("name", "validArtistName")
                        .param("artists", "1")
                        .param("artists", "2")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    @WithMockUser
    void testNonAdminAddAlbumForbidden() throws Exception {
        mockMvc.perform(multipart("/albums/add")
                        .with(csrf()))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(
            roles = {"ADMIN", "USER"}
    )
    void testAdminRemoveAlbum() throws Exception {
        mockMvc.perform(delete("/albums/remove/{id}", album.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/albums/all"));
    }

    @Test
    @WithMockUser
    void NonAdminRemoveAlbumForbidden() throws Exception {

        mockMvc.perform(delete("/albums/remove/{id}", album.getId())
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void testAnonymousGetAllAlbums() throws Exception {

        mockMvc.perform(get("/albums/all"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("title", "Albums"))
                .andExpect(model().attribute("albums", hasSize(initService.getAlbums().size())))
                .andExpect(model().attribute("albums", hasItem(
                        allOf(hasProperty("name", is(album.getName()))
                                , hasProperty("artists", hasSize(album.getArtists().size()))
                        ))))
                .andExpect(view().name("view-all"));
    }

    @Test
    @WithMockUser
    void testAuthenticatedGetAlbum() throws Exception {
        Song song = initService.getSong();
        album.setSongs(Set.of(song));
        mockMvc.perform(get("/albums/view/{id}", album.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("title", "Songs"))
                .andExpect(model().attribute("songs", hasSize(album.getSongs().size())))
                .andExpect(view().name("view-all"));
    }

    @Test
    @WithAnonymousUser
    void testAnonymousGetAlbum() throws Exception {
        Song song = initService.getSong();
        album.setSongs(Set.of(song));
        mockMvc.perform(get("/albums/view/{id}", album.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("title", "Songs"))
                .andExpect(model().attribute("songs", hasSize(album.getSongs().size())))
                .andExpect(view().name("view-all"));
    }
}
