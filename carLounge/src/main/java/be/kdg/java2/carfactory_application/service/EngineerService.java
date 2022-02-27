package be.kdg.java2.carfactory_application.service;

import be.kdg.java2.carfactory_application.domain.Car;
import be.kdg.java2.carfactory_application.domain.Engineer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EngineerService {

    void addEngineer(Engineer engineer);

    void addEngineerWithNewCar(Engineer engineer, Car car, MultipartFile multipartFile) throws IOException;

    void deleteEngineer(Engineer engineer);

    List<Engineer> getAllEngineers();

    Engineer findById(int id);


    List<Engineer> findByNameContains(String lookup);

    List<Engineer> findByTenureIsGreaterThanEqual(int tenure);

    List<Engineer> findByTenureIsLessThanEqual(int tenure);

    List<Engineer> findByTenure(int tenure);

    void update(Engineer changedEngineer);

    void addContributionToEngineer(Car car, Engineer newEngineer);

//    List<Engineer> getEngineerOnTradeMark(String trademark);
}