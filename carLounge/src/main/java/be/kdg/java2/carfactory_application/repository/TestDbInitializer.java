package be.kdg.java2.carfactory_application.repository;

import be.kdg.java2.carfactory_application.domain.factory.*;
import be.kdg.java2.carfactory_application.domain.user.Gender;
import be.kdg.java2.carfactory_application.domain.user.Role;
import be.kdg.java2.carfactory_application.domain.user.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Profile("seeding")
@Repository
public class TestDbInitializer implements CommandLineRunner {

    private final TradeMarkRepositorySDR tradeMarkRepository;
    private final ContributionRepository contributionRepository;
    private final EngineerRepositorySDR engineerRepositorySDR;
    private final UserRepository userRepository;

    public TestDbInitializer(TradeMarkRepositorySDR tradeMarkRepository,
                             ContributionRepository contributionRepository,
                             EngineerRepositorySDR engineerRepositorySDR,
                             UserRepository userRepository) {
        this.tradeMarkRepository = tradeMarkRepository;
        this.contributionRepository = contributionRepository;
        this.engineerRepositorySDR = engineerRepositorySDR;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        //Users
        User sami = new User("sami", "$2a$10$ub8V55Kei4eABgcyftwCpOz9toW9avhiUIOxl8dgY8s3OFIN6FQUG",
                Gender.MALE, "Algerian", 23, Role.ADMIN);
        //Trademarks
        TradeMark mercedes = new TradeMark("Mercedes-Benz", "Karl Benz", 1926);

        //Engineers section
        Engineer frank = new Engineer("Frank Lamberty", 18, "German");


        //Creating car objects
        Car classA = new Car("A-Class", 2.0, 32500, java.time.LocalDate.of(2019, 8, 31), Color.WHITE);
        classA.setImage("aClass.jpg");
        classA.setTradeMark(mercedes);

        Car classC = new Car("C-Class", 2.0, 41400, java.time.LocalDate.of(2019, 12, 14), Color.CHROME);
        classC.setImage("cClass.jpg");
        classC.setTradeMark(mercedes);

        //Setting authors for cars
        classA.setAuthor(sami);
        classC.setAuthor(sami);
        frank.setAuthor(sami);

        mercedes.getCars().addAll(Arrays.asList(classA, classC));


        List<Contribution> contributions = List.of(
                new Contribution(classA, frank));

//     Persist
        //Inserts
        userRepository.saveAll(List.of(sami));
        tradeMarkRepository.saveAll(List.of(mercedes));
        engineerRepositorySDR.saveAll(List.of(frank));
        contributionRepository.saveAll(contributions);

    }
}
