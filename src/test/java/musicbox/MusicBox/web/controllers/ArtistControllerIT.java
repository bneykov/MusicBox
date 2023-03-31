package musicbox.MusicBox.web.controllers;

import musicbox.MusicBox.model.entity.Album;
import musicbox.MusicBox.model.entity.Artist;
import musicbox.MusicBox.services.init.InitService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ArtistControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private InitService initService;

    private Artist artist;


    @BeforeEach
    void setUp() {
        this.initService.init();
        Album album = this.initService.getAlbum();
        Album album2 = this.initService.getAlbum2();
        this.artist = this.initService.getArtist();
        this.artist.setAlbums(Set.of(album, album2));

    }

    @AfterEach
    void tearDown(){
        this.initService.clearDatabase();
    }

    @Test
    @WithMockUser(
            roles = {"ADMIN", "USER"}
    )
    void testGetAddArtistPageShown() throws Exception {
        mockMvc.perform(get("/artists/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-artist"));
    }

    @Test
    @WithMockUser
    void testGetAddArtistPageForbidden() throws Exception {
        mockMvc.perform(get("/artists/add"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(
            roles = {"ADMIN", "USER"}
    )
    void testAdminAddArtistFail() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "test.jpeg",
                "image/jpeg",
                new byte[]{}
        );
        mockMvc.perform(multipart("/artists/add")
                        .file(file) //cannot be empty
                        .param("name", "validArtistName")
                        .param("description", "testDescription")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("artistDTO"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.artistDTO"))
                .andExpect(model().attributeHasErrors())
                .andExpect(redirectedUrl("/artists/add"));
    }

    @Test
    @WithMockUser(
            roles = {"ADMIN", "USER"}
    )
    void testAdminRemoveArtist() throws Exception {
        mockMvc.perform(delete("/artists/remove/{id}", artist.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/artists/all"));
    }

    @Test
    @WithMockUser
    void testNonAdminRemoveArtistForbidden() throws Exception {
        mockMvc.perform(delete("/artists/remove/{id}", artist.getId())
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void testAuthenticatedGetAllArtists() throws Exception {
        mockMvc.perform(get("/artists/all"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("title", "Artists"))
                .andExpect(model().attribute("artists", hasSize(initService.getArtists().size())))
                .andExpect(view().name("view-all"));
    }

    @Test
    @WithAnonymousUser
    void testAnonymousGetAllArtists() throws Exception {
        mockMvc.perform(get("/artists/all"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("title", "Artists"))
                .andExpect(model().attribute("artists", hasSize(initService.getArtists().size())))
                .andExpect(view().name("view-all"));
    }

    @Test
    @WithMockUser
    void testAuthenticatedGetArtist() throws Exception {
        mockMvc.perform(get("/artists/view/{id}", artist.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("title", "Albums"))
                .andExpect(model().attributeExists("artist"))
                .andExpect(model().attribute("albums", hasSize(artist.getAlbums().size())))
                .andExpect(view().name("view-all"));
    }

    @Test
    @WithAnonymousUser
    void testAnonymousGetArtist() throws Exception {
        mockMvc.perform(get("/artists/view/{id}", artist.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("title", "Albums"))
                .andExpect(model().attribute("albums", hasSize(artist.getAlbums().size())))
                .andExpect(view().name("view-all"));
    }
}
