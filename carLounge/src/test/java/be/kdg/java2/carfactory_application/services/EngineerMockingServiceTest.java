package be.kdg.java2.carfactory_application.services;

import be.kdg.java2.carfactory_application.domain.factory.Engineer;
import be.kdg.java2.carfactory_application.domain.user.Gender;
import be.kdg.java2.carfactory_application.domain.user.Role;
import be.kdg.java2.carfactory_application.domain.user.User;
import be.kdg.java2.carfactory_application.exception.EntityAlreadyExistsException;
import be.kdg.java2.carfactory_application.exception.InvalidImageException;
import be.kdg.java2.carfactory_application.exception.UserAlreadyExistException;
import be.kdg.java2.carfactory_application.repository.EngineerRepositorySDR;
import be.kdg.java2.carfactory_application.service.EngineerService;
import be.kdg.java2.carfactory_application.service.EngineerServiceImplementation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@SpringBootTest
public class EngineerMockingServiceTest {

    @Autowired
    private EngineerServiceImplementation engineerService;

    @MockBean
    private EngineerRepositorySDR engineerRepository;

    @Test
    public void addingAnAlreadyExistingEngineerShouldThrowAnException() {
        // Arrange
        var issam = new User("issam1", "$2a$10$ub8V55Kei4eABgcyftwCpOz9toW9avhiUIOxl8dgY8s3OFIN6FQUG",
                Gender.MALE, "Algerian", 23, Role.MANAGER);

        var engineer = new Engineer("Issam", 7, "Algerian");
        engineer.setAuthor(issam);
        given(engineerRepository.findByNameIgnoreCase(engineer.getName())).willReturn(Optional.of(engineer));
        var engineer1 = new Engineer("Issam", 7, "Algerian");
        engineer1.setAuthor(issam);

        // Assert + act
        assertThrows(EntityAlreadyExistsException.class, () -> {
            engineerService.addEngineer(engineer1);
        });

        verify(engineerRepository, times(0))
                .save(any(Engineer.class));
    }

    @Test
    public void retrievingAnNonExistingEngineerShouldReturnNull() {
        // Arrange
        given(engineerRepository.findById(1))
                .willReturn(Optional.empty());

        // Act
        var returnedEngineer = engineerService.findById(1);
        // Assert
        assertNull(returnedEngineer);
    }

    @Test
    public void retrievingAnANExistingEngineerShouldReturnAValidEngineer() {
        // Arrange
        var engineer = new Engineer("issam", 7, "Algerian");
        given(engineerRepository.findById(1))
                .willReturn(Optional.of(engineer));
        // Act
        var returnedEngineer = engineerService.findById(1);
        // Assert
        assertNotNull(returnedEngineer);
    }
}
