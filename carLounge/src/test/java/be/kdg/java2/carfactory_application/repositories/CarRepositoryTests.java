package be.kdg.java2.carfactory_application.repositories;

import be.kdg.java2.carfactory_application.domain.factory.Car;
import be.kdg.java2.carfactory_application.domain.factory.Color;
import be.kdg.java2.carfactory_application.domain.factory.TradeMark;
import be.kdg.java2.carfactory_application.domain.user.Gender;
import be.kdg.java2.carfactory_application.domain.user.Role;
import be.kdg.java2.carfactory_application.domain.user.User;
import be.kdg.java2.carfactory_application.repository.CarRepositorySDR;
import be.kdg.java2.carfactory_application.repository.TradeMarkRepositorySDR;
import be.kdg.java2.carfactory_application.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CarRepositoryTests {
    @Autowired
    private TradeMarkRepositorySDR tradeMarkRepository;

    @Autowired
    private CarRepositorySDR carRepositorySDR;

    @Autowired
    private UserRepository userRepository;

    protected TradeMark mercedes;

    @BeforeAll
    public void setup() {
        var issam = new User("issam1", "$2a$10$ub8V55Kei4eABgcyftwCpOz9toW9avhiUIOxl8dgY8s3OFIN6FQUG",
                Gender.MALE, "Algerian", 23, Role.MANAGER);
        userRepository.save(issam);
        mercedes = new TradeMark("Mercedes-Benz", "Karl Benz", 1926);
        var classC = new Car("C-ClassZ", 2.0, 41400, java.time.LocalDate.of(2019, 12, 14), Color.CHROME);
        var classA = new Car("A-ClassZ", 2.0, 41400, java.time.LocalDate.of(2012, 5, 2), Color.BLACK);

        //set Authors
        classC.setAuthor(issam);
        mercedes.addCar(classC);
    }

    @Test
    @Transactional
//    Edge case is somewhat useless (?) If i don't add a car to a trademark, it surely won't be persisted
    public void persistingATradeMarkShouldPersistAssociatedCars() {
      // Act
        tradeMarkRepository.save(mercedes);
                //Arrange
           var car = carRepositorySDR.findByModelContainsIgnoreCase("C-ClassZ");
           var car2 = carRepositorySDR.findByModelContainsIgnoreCase("A-ClassZ");
        // Assert
        assertNotNull(car);
        assertEquals("C-ClassZ", car.getModel());
        assertNull(car2);
    }

}
