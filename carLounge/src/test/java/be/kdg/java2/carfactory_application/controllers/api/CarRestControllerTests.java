package be.kdg.java2.carfactory_application.controllers.api;

import be.kdg.java2.carfactory_application.domain.factory.Car;
import be.kdg.java2.carfactory_application.domain.factory.Color;
import be.kdg.java2.carfactory_application.domain.factory.Engineer;
import be.kdg.java2.carfactory_application.domain.user.Gender;
import be.kdg.java2.carfactory_application.domain.user.Role;
import be.kdg.java2.carfactory_application.domain.user.User;
import be.kdg.java2.carfactory_application.repository.CarRepositorySDR;
import be.kdg.java2.carfactory_application.repository.EngineerRepositorySDR;
import be.kdg.java2.carfactory_application.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class CarRestControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarRepositorySDR carRepository;

    @Autowired
    private UserRepository userRepository;


    @BeforeAll
    public void setup() {
        User sami = new User("sami", "$2a$10$ub8V55Kei4eABgcyftwCpOz9toW9avhiUIOxl8dgY8s3OFIN6FQUG",
                Gender.MALE, "Algerian", 23, Role.ADMIN);
        userRepository.save(sami);

        Car classA = new Car("A-Class", 2.0, 32500, java.time.LocalDate.of(2019, 8, 31), Color.WHITE);
        Car classB = new Car("B-Class", 4.0, 143000, java.time.LocalDate.of(2018, 1, 4), Color.BLUE);
        classA.setAuthor(sami);
        classB.setAuthor(sami);
        carRepository.saveAll(List.of(classA, classB));
    }

    @Test
    public void fetchCarsByNameShouldBeHandledProperlyAndReturnStatusOK() throws Exception {
        // Act + Assert
        mockMvc.perform(
                        get("/api/cars?lookup=Class")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON.toString()));
    }


    @Test
    public void fetchCarsByNameShouldReturnContentEmptyIfModelDoesntExist() throws Exception {
        // Act + Assert
        mockMvc.perform(
                        get("/api/cars?lookup=potato")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }
}
