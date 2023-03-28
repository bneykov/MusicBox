package musicbox.MusicBox.web.controllers;

import musicbox.MusicBox.model.entity.UserEntity;
import musicbox.MusicBox.services.init.InitService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GetUsersControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private InitService initService;

    private UserEntity user, admin;

    @BeforeEach
    void setUp() {
        user = this.initService.getUser();
        admin = this.initService.getAdmin();
        this.initService.init();
    }

    @AfterEach
    void tearDown() {
        this.initService.clearDatabase();
    }

    @Test
    @WithMockUser
    void testNonAdminGetUsersDataForbidden() throws Exception {
        mockMvc.perform(get("/users/get"))
                .andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(
            roles = {"ADMIN", "USER"}
    )
    void testAdminGetUsersDataSuccess() throws Exception {
        mockMvc.perform(get("/users/get"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(initService.getUsers().size())))
                .andExpect(jsonPath("$[0].username").value(admin.getUsername()))
                .andExpect(jsonPath("$[1].username").value(user.getUsername()));
    }

}
