package be.kdg.java2.carfactory_application.repositories;

import be.kdg.java2.carfactory_application.domain.factory.Car;
import be.kdg.java2.carfactory_application.domain.factory.Color;
import be.kdg.java2.carfactory_application.domain.factory.TradeMark;
import be.kdg.java2.carfactory_application.domain.user.Gender;
import be.kdg.java2.carfactory_application.domain.user.Role;
import be.kdg.java2.carfactory_application.domain.user.User;
import be.kdg.java2.carfactory_application.repository.CarRepositorySDR;
import be.kdg.java2.carfactory_application.repository.TradeMarkRepositorySDR;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CarRepositoryTests {
    @Autowired
    private TradeMarkRepositorySDR tradeMarkRepository;

    @Autowired
    private CarRepositorySDR carRepositorySDR;

    TradeMark mercedes;

    @BeforeAll
    public void setup() {
        var issam = new User("issam", "$2a$10$ub8V55Kei4eABgcyftwCpOz9toW9avhiUIOxl8dgY8s3OFIN6FQUG",
                Gender.MALE, "Algerian", 23, Role.MANAGER);
        mercedes = new TradeMark("Mercedes-Benz", "Karl Benz", 1926);

        var classC = new Car("C-Class", 2.0, 41400, java.time.LocalDate.of(2019, 12, 14), Color.CHROME);
        var classA = new Car("A-Class", 2.0, 32500, java.time.LocalDate.of(2019, 8, 31), Color.WHITE);
        //set Authors
        classC.setAuthor(issam);
        classA.setAuthor(issam);
        //
        mercedes.addCar(classC);
        mercedes.addCar(classA);
        //Persist

    }

    @Test
    public void persistingATradeMarkShouldPersistAssociatedCars() {
        // Arrange
        var car1 = carRepositorySDR.findByModelContainsIgnoreCase("a-class");
        var car2 = carRepositorySDR.findByModelContainsIgnoreCase("c-class");
        // Act
        tradeMarkRepository.save(mercedes);
        // Assert
       assertEquals(1,1);


    }
}
