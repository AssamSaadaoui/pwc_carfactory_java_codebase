package be.kdg.java2.carfactory_application.controllers.mvc;


import be.kdg.java2.carfactory_application.domain.factory.Car;
import be.kdg.java2.carfactory_application.domain.factory.Color;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class CarMvcControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepositorySDR carRepository;

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
    void showCarDetailsShouldReturnTheRightViewModel() throws Exception {
        mockMvc.perform(
                        get("/cars/{id}", "1")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("/cars/cardetails"))
                .andExpect(model().attribute("car",
                        hasProperty("model", equalTo("A-Class"))));
    }

    @Test
    void showCarDetailsShouldNotReturnACorrectViewModel() throws Exception {
        mockMvc.perform(
                        get("/cars/{id}", "99")
                )
                .andExpect(status().isNotFound())
                .andExpect(view().name("/cars/cardetails"));
    }
}
