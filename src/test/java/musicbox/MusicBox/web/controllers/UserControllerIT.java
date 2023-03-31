package musicbox.MusicBox.web.controllers;

import musicbox.MusicBox.model.entity.UserEntity;
import musicbox.MusicBox.model.enums.RoleEnum;
import musicbox.MusicBox.services.init.InitService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private InitService initService;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        this.initService.init();
        this.testUser = this.initService.getUser();
    }

    @AfterEach
    void tearDown() {
        this.initService.clearDatabase();
    }

    @Test
    void testUserRegistrationSuccess() throws Exception {

        mockMvc.perform(post("/users/register")
                        .param("username", "testUsername")
                        .param("email", "test@abv.bg")
                        .param("name", "testName")
                        .param("password", "topsecret")
                        .param("confirmPassword", "topsecret")
                        .with(csrf())).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"))
                .andExpect(flash().attributeCount(0));

    }

    @Test
    void testUserRegistrationFail() throws Exception {

        mockMvc.perform(post("/users/register")
                        .param("username", "testUsername")
                        .param("email", "test@abv.bg")
                        .param("name", "testName")
                        .param("password", "topsecret")
                        .param("confirmPassword", "topsecret1") //wrong confirmPass
                        .with(csrf())).andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("registerDTO"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.registerDTO"))
                .andExpect(model().attributeHasErrors())
                .andExpect(redirectedUrl("/users/register"));

    }

    @Test
    void testRegisterPageShown() throws Exception {
        mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));

    }

    @Test
    void testLoginPageShown() throws Exception {
        mockMvc.perform(get("/users/login")).
                andExpect(status().isOk()).
                andExpect(view().name("login"));

    }

    @Test
    @WithMockUser
    void testLoginPageForbidden() throws Exception {
        mockMvc.perform(get("/users/login")).
                andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser
    void testRegisterPageForbidden() throws Exception {
        mockMvc.perform(get("/users/register")).
                andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void testFailedLogin() throws Exception {
        mockMvc.perform(post("/login-error")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN", "USER"}
    )
    void testAdminGetAllUsers() throws Exception {
        mockMvc.perform(get("/users/all"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("roles", List.of(RoleEnum.ADMIN, RoleEnum.USER).toArray()))
                .andExpect(view().name("all-users"));
    }

    @Test
    @WithMockUser(
            username = "user",
            roles = "USER"
    )
    void testNonAdminGetAllUsers() throws Exception {
        mockMvc.perform(get("/users/all"))
                .andExpect(status().isForbidden());
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
