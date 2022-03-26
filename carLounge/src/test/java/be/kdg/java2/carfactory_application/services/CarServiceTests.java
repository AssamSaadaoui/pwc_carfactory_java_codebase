package be.kdg.java2.carfactory_application.services;


import be.kdg.java2.carfactory_application.domain.factory.Car;
import be.kdg.java2.carfactory_application.domain.factory.Color;
import be.kdg.java2.carfactory_application.domain.factory.Engineer;
import be.kdg.java2.carfactory_application.domain.user.Gender;
import be.kdg.java2.carfactory_application.domain.user.Role;
import be.kdg.java2.carfactory_application.domain.user.User;
import be.kdg.java2.carfactory_application.exception.EntityAlreadyExistsException;
import be.kdg.java2.carfactory_application.exception.InvalidImageException;
import be.kdg.java2.carfactory_application.service.CarService;
import be.kdg.java2.carfactory_application.service.EngineerService;
import be.kdg.java2.carfactory_application.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

import java.io.File;
import java.io.IOException;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class CarServiceTests {
    @Autowired
    private CarService carService;

    @Autowired
    private UserService userService;

    User issam;

    @BeforeAll
    public void setup() {
        issam = new User("issam1", "$2a$10$ub8V55Kei4eABgcyftwCpOz9toW9avhiUIOxl8dgY8s3OFIN6FQUG",
                Gender.MALE, "Algerian", 23, Role.MANAGER);
        userService.save(issam);
    }

    @Transactional
    @Test
    public void saveCarWithoutAnImageShouldThrowAnException() {
        // Arrange
        var classC = new Car("C-ClassZ", 2.0, 41400, java.time.LocalDate.of(2019, 12, 14), Color.CHROME);
        // Whatever file
        MultipartFile multipartFile = new MockMultipartFile("file", "", MediaType.TEXT_PLAIN_VALUE, "Hello World" .getBytes());        // Act + Assert
        classC.setAuthor(issam);
        // Assert
        assertThrows(InvalidImageException.class, () -> {
            // Act
            carService.addCar(classC, multipartFile);
        });
    }
    @Transactional
    @Test
    public void saveCarWithAnImageShouldPass() throws IOException {
        // Arrange
        var classC = new Car("C-ClassZ", 2.0, 41400, java.time.LocalDate.of(2019, 12, 14), Color.CHROME);
        // Whatever file
        MultipartFile multipartFile = new MockMultipartFile("file", "carImg.jpg", MediaType.TEXT_PLAIN_VALUE, "Hello World" .getBytes());        // Act + Assert
        classC.setAuthor(issam);
        // Act
        carService.addCar(classC, multipartFile);
        var car = carService.findByModel("C-ClassZ");
        // Assert
        assertNotNull(car);
    }

}
