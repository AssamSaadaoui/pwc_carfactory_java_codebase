package be.kdg.java2.carfactory_application.controllers.mvc;

import be.kdg.java2.carfactory_application.domain.factory.Car;
import be.kdg.java2.carfactory_application.domain.factory.Color;
import be.kdg.java2.carfactory_application.domain.factory.Contribution;
import be.kdg.java2.carfactory_application.domain.factory.Engineer;
import be.kdg.java2.carfactory_application.domain.user.Flag;
import be.kdg.java2.carfactory_application.domain.user.Gender;
import be.kdg.java2.carfactory_application.domain.user.Role;
import be.kdg.java2.carfactory_application.domain.user.User;
import be.kdg.java2.carfactory_application.security.CustomUserDetailsService;
import be.kdg.java2.carfactory_application.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@ActiveProfiles("seeding")
public class UserMvcControllerMockingTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImplementation userService;


    @Test
    @WithMockUser(username = "sami", roles = {"ADMIN"})
    public void adminUserShouldBeAbleToDisableUsers() throws Exception {
        var issam = new User("issam", "$2a$10$ub8V55Kei4eABgcyftwCpOz9toW9avhiUIOxl8dgY8s3OFIN6FQUG",
                Gender.MALE, "Algerian", 23, Role.USER);
        given(userService.findUser(1)).willReturn(issam);
        mockMvc.perform(
                        post("/users/disable/{id}", 1)
                                .with(csrf())
                )
                .andExpect(redirectedUrl("/users?success=true&flag=locked"));
        assertEquals(issam.getFlag(), Flag.DISABLED);
    }
}

