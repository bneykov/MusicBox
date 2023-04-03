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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ManageUsersControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private InitService initService;

    private UserEntity testUser, testAdmin;

    @BeforeEach
    void setUp() {
        this.initService.init();
        testUser = this.initService.getUser();
        testAdmin = this.initService.getAdmin();
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
                .andExpect(jsonPath("$[0].username").value(testAdmin.getUsername()))
                .andExpect(jsonPath("$[1].username").value(testUser.getUsername()));
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN", "USER"}
    )
    void testAdminChangeRole() throws Exception {
        mockMvc.perform(get("/users/{id}/change_role", testUser.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/all"));
    }

    @Test
    @WithMockUser
    void testNonAdminChangeRole() throws Exception {
        mockMvc.perform(get("/users/{id}/change_role", testUser.getId()))
                .andExpect(status().isForbidden());
    }

}
