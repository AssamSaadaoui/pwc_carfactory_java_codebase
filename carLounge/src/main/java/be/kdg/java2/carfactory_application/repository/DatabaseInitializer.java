package be.kdg.java2.carfactory_application.repository;

import be.kdg.java2.carfactory_application.domain.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;


//@Profile("SDR")
@Repository
public class DatabaseInitializer implements CommandLineRunner {

    private final TradeMarkRepositorySDR tradeMarkRepository;
    private final ContributionRepository contributionRepository;
    private final EngineerRepositorySDR engineerRepositorySDR;
    private final UserRepository userRepository;

    public DatabaseInitializer(TradeMarkRepositorySDR tradeMarkRepository, ContributionRepository contributionRepository, EngineerRepositorySDR engineerRepositorySDR, UserRepository userRepository) {
        this.tradeMarkRepository = tradeMarkRepository;
        this.contributionRepository = contributionRepository;
        this.engineerRepositorySDR = engineerRepositorySDR;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        User issam = new User("issam", "$2a$10$ub8V55Kei4eABgcyftwCpOz9toW9avhiUIOxl8dgY8s3OFIN6FQUG");
        TradeMark mercedes = new TradeMark("Mercedes-Benz", "Karl Benz", 1926);
        TradeMark bmw = new TradeMark("BMW", "Camillo Castiglioni", 1916);
        TradeMark audi = new TradeMark("Audi", "August Horch", 1909);
        TradeMark volkswagen = new TradeMark("Volkswagen", "German Labour Front", 1937);
        TradeMark ford = new TradeMark("Ford", "Henry Ford", 1903);
        TradeMark toyota = new TradeMark("Toyota", "AKiichiro Toyoda", 1937);

        //        Engineers section
        Engineer frank = new Engineer("Frank Lamberty", 18, "German");
        Engineer christopher = new Engineer("Christopher E. Bangle", 10, "American");
//        Engineer christian = new Engineer("Christian Früh", 21, "German");
//        Engineer rishi = new Engineer("Rishi Chavda", 15, "German");
//        Engineer wonJu = new Engineer("Won Ju", 12, "Korean");
//        Engineer bern = new Engineer("Bern Hard", 7, "American");
//        Engineer muller = new Engineer("Heren Muller", 10, "German");
//        Engineer korn = new Engineer("Winter Korn", 14, "American");

        frank.setAuthor(issam);

        Car classA = new Car("A-Class", 2.0, 32500, java.time.LocalDate.of(2019, 8, 31), Color.WHITE);
        classA.setImage("aClass.jpg");
//        classA.getEngineers().addAll(List.of(frank));


        classA.setAuthor(issam);
//        christian
//        frank.addCar(classA);
//        christian.addCar(classA);
        classA.setTradeMark(mercedes);

        Car classC = new Car("C-Class", 2.0, 41400, java.time.LocalDate.of(2019, 12, 14), Color.CHROME);
        classC.setImage("cClass.jpg");
        classC.setAuthor(issam);
//        classC.getEngineers().addAll(List.of(christian, korn));
//        christian.addCar(classC);
//        korn.addCar(classC);
        classC.setTradeMark(mercedes);
//
//        Car audiR8 = new Car("Audi R8", 4.0, 143000, java.time.LocalDate.of(2018, 1, 4), Color.BLUE);
//        audiR8.setImage("audiR8.jpg");
//        audiR8.addEngineer(christopher);
//        christopher.addCar(audiR8);
//        audiR8.setTradeMark(audi);
//
//        Car bmw7 = new Car("7 Series", 4.4, 134000, java.time.LocalDate.of(2021, 10, 1), Color.GRAY);
//        bmw7.setImage("bmw7.jpg");
//        bmw7.getEngineers().addAll(List.of(rishi, wonJu));
//        rishi.addCar(bmw7);
//        wonJu.addCar(bmw7);
//        bmw7.setTradeMark(bmw);
//
//        Car bmw8 = new Car("8 Series", 4.0, 14000, java.time.LocalDate.of(2022, 2, 4), Color.BLACK);
//        bmw8.setImage("bmw8.png");
//        bmw8.addEngineer(wonJu);
//        wonJu.addCar(bmw8);
//        bmw8.setTradeMark(bmw);
//
//        Car golf8 = new Car("Golf 8", 2.0, 25300, java.time.LocalDate.of(2021, 2, 5), Color.WHITE);
//        golf8.setImage("golf8.jpg");
//        golf8.addEngineer(muller);
//        muller.addCar(golf8);
//        golf8.setTradeMark(volkswagen);
//
//        Car chr = new Car("C-HR", 4.8, 25300, java.time.LocalDate.of(2021, 2, 5), Color.WHITE);
//        chr.setImage("chr.jpg");
//        chr.addEngineer(bern);
//        bern.addCar(chr);
//        chr.setTradeMark(toyota);
//
//        Car fordSE = new Car("Fiesta SE", 1.2, 16000, java.time.LocalDate.of(2017, 6, 10), Color.WHITE);
//        fordSE.setImage("fordSE.jpg");
//        fordSE.addEngineer(korn);
//        korn.addCar(fordSE);
//        fordSE.setTradeMark(ford);

        mercedes.getCars().addAll(Arrays.asList(classA, classC));
//        bmw.addCar(bmw7);
//        bmw.addCar(bmw8);
//        audi.addCar(audiR8);
//        volkswagen.addCar(golf8);
//        toyota.addCar(chr);
//        ford.addCar(fordSE);


//     Persist
        //Inserts
        userRepository.save(issam);
        tradeMarkRepository.saveAll(Arrays.asList(mercedes, bmw, audi, volkswagen, toyota, ford));
        engineerRepositorySDR.save(frank);
        contributionRepository.saveAll(List.of(new Contribution(classA, frank)));

    }
}
