package be.kdg.java2.carfactory_application.repositories;

import be.kdg.java2.carfactory_application.domain.factory.*;
import be.kdg.java2.carfactory_application.domain.user.Gender;
import be.kdg.java2.carfactory_application.domain.user.Role;
import be.kdg.java2.carfactory_application.domain.user.User;
import be.kdg.java2.carfactory_application.repository.CarRepositorySDR;
import be.kdg.java2.carfactory_application.repository.ContributionRepository;
import be.kdg.java2.carfactory_application.repository.EngineerRepositorySDR;
import be.kdg.java2.carfactory_application.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContributionRepositoryTests {
    @Autowired
    private EngineerRepositorySDR engineerRepositorySDR;

    @Autowired
    private CarRepositorySDR carRepositorySDR;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContributionRepository contributionRepository;


    @BeforeAll
    public void setup() {
        var issam = new User("issam1", "$2a$10$ub8V55Kei4eABgcyftwCpOz9toW9avhiUIOxl8dgY8s3OFIN6FQUG",
                Gender.MALE, "Algerian", 23, Role.MANAGER);
        userRepository.save(issam);
        var classC = new Car("C-ClassZ", 2.0, 41400, java.time.LocalDate.of(2019, 12, 14), Color.CHROME);
        classC.setAuthor(issam);
        carRepositorySDR.save(classC);
        var korn = new Engineer("Winter Korn", 14, "American");
        korn.setAuthor(issam);
        engineerRepositorySDR.save(korn);
        var collaboration = new Contribution(classC, korn);
        contributionRepository.save(collaboration);
    }

    @Test
    @Transactional
//    deleting a car or engineer should cascade to contribution
    public void deletingAPartOfContributionShouldDeleteTheContribution() {
        // Arrange
        var car = carRepositorySDR.findByModelContainsIgnoreCase("C-ClassZ");
        // Act
        carRepositorySDR.delete(car);
        var contribution = contributionRepository.findById(4).orElse(null);
        // Assert
        assertNull(contribution);
    }

    @Test
    @Transactional
//    deleting contribution should not cascade to car or engineer
    public void deletingContributionShouldNotDeleteTheCarAndEngineerPartOfIt() {
        // Arrange
        var contribution = contributionRepository.findById(4).orElse(null);
        // Act
        contributionRepository.delete(contribution);
        var car = carRepositorySDR.findByModelContainsIgnoreCase("C-ClassZ");
        // Assert
        assertNotNull(car);
    }
}
