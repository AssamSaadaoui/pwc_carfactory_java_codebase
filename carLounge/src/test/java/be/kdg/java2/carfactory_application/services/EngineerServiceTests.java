package be.kdg.java2.carfactory_application.services;


import be.kdg.java2.carfactory_application.domain.factory.Engineer;
import be.kdg.java2.carfactory_application.domain.user.Gender;
import be.kdg.java2.carfactory_application.domain.user.Role;
import be.kdg.java2.carfactory_application.domain.user.User;
import be.kdg.java2.carfactory_application.exception.EntityAlreadyExistsException;
import be.kdg.java2.carfactory_application.service.EngineerService;
import be.kdg.java2.carfactory_application.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class EngineerServiceTests {
    @Autowired
    private EngineerService engineerService;

    @Autowired
    private UserService userService;

    User issam;

    @BeforeAll
    public void setup() {
        issam = new User("issam1", "$2a$10$ub8V55Kei4eABgcyftwCpOz9toW9avhiUIOxl8dgY8s3OFIN6FQUG",
                Gender.MALE, "Algerian", 23, Role.MANAGER);
        userService.save(issam);
        var korn = new Engineer("Winter Korn", 14, "American");
        korn.setAuthor(issam);
        engineerService.addEngineer(korn);
    }

    @Transactional
    @Test
    public void saveEngineerThrowsExceptionWhenEngineerAlreadyExists() {
        // Arrange
        var korn1 = new Engineer("Winter Korn", 16, "American");
        korn1.setAuthor(issam);
        // Assert
        assertThrows(EntityAlreadyExistsException.class, () -> {
            // Act
            engineerService.addEngineer(korn1);
        });
    }

    @Transactional
    @Test
    public void saveEngineerShouldPassForNotExistingEngineer() {
        // Arrange
        var william = new Engineer("William", 14, "German");
        william.setAuthor(issam);
        //Act
        engineerService.addEngineer(william);
        // Assert
        assertFalse(engineerService.findByNameContains(william.getName()).isEmpty());
    }
}
