package be.kdg.java2.carfactory_application.repositories;

import be.kdg.java2.carfactory_application.domain.factory.Car;
import be.kdg.java2.carfactory_application.domain.factory.Color;
import be.kdg.java2.carfactory_application.domain.factory.Engineer;
import be.kdg.java2.carfactory_application.domain.factory.TradeMark;
import be.kdg.java2.carfactory_application.domain.user.Gender;
import be.kdg.java2.carfactory_application.domain.user.Role;
import be.kdg.java2.carfactory_application.domain.user.User;
import be.kdg.java2.carfactory_application.repository.CarRepositorySDR;
import be.kdg.java2.carfactory_application.repository.EngineerRepositorySDR;
import be.kdg.java2.carfactory_application.repository.TradeMarkRepositorySDR;
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
public class EngineerRepositoryTests {
    @Autowired
    private EngineerRepositorySDR engineerRepositorySDR;

    @Autowired
    private CarRepositorySDR carRepositorySDR;

    @Autowired
    private UserRepository userRepository;


    @BeforeAll
    public void setup() {
        var issam = new User("issam1", "$2a$10$ub8V55Kei4eABgcyftwCpOz9toW9avhiUIOxl8dgY8s3OFIN6FQUG",
                Gender.MALE, "Algerian", 23, Role.MANAGER);
        userRepository.save(issam);
        var mercedes = new TradeMark("Mercedes-Benz", "Karl Benz", 1926);
        var classC = new Car("C-ClassZ", 2.0, 41400, java.time.LocalDate.of(2019, 12, 14), Color.CHROME);
        classC.setTradeMark(mercedes);
        classC.setAuthor(issam);
        carRepositorySDR.save(classC);
        var korn = new Engineer("Winter Korn", 14, "American");
        korn.setAuthor(issam);
        engineerRepositorySDR.save(korn);
    }

//    @Test
//    public void findEngineersShouldNotFetchContributionsWithThem() {
//        // Act
//        var engineersList = engineerRepositorySDR.findByNameContainsIgnoreCase("winter korn");
//        // Assert
//        engineersList.forEach(engineer -> {
//            if (engineer.getName().equalsIgnoreCase("winter korn")) {
//                assertEquals(engineer.getName().toLowerCase(Locale.ROOT), "winter korn");
//                assertTrue(engineer.getContributions().isEmpty());
//            }
//        });
//    }

    @Test
    public void findEngineersShouldFetchAuthorWithThem() {
        // Act
        var engineersList = engineerRepositorySDR.findByNameContainsIgnoreCase("winter korn");
        // Assert
        engineersList.forEach(engineer -> {
            if (engineer.getName().equalsIgnoreCase("winter korn")) {
                assertEquals(engineer.getName().toLowerCase(Locale.ROOT), "winter korn");
                assertNotNull(engineer.getAuthor());
//                assertNull(engineer.getAuthor());
            }
        });
    }

}
