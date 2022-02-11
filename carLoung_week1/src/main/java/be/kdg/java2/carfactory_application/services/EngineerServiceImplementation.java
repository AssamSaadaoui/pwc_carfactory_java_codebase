package be.kdg.java2.carfactory_application.services;


import be.kdg.java2.carfactory_application.domain.Car;
import be.kdg.java2.carfactory_application.domain.Engineer;
import be.kdg.java2.carfactory_application.exceptions.EntityAlreadyExistsException;
import be.kdg.java2.carfactory_application.repository.CarRepositorySDR;
import be.kdg.java2.carfactory_application.repository.EngineerRepositorySDR;
import be.kdg.java2.carfactory_application.repository.TradeMarkRepositorySDR;
import be.kdg.java2.carfactory_application.util.FileUploadUtil;
import be.kdg.java2.carfactory_application.util.ServiceChecksUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
public class EngineerServiceImplementation implements EngineerService {

    private final EngineerRepositorySDR engineerRepository;
    private final CarRepositorySDR carRepository;
    private final TradeMarkRepositorySDR tradeMarkRepository;

    @Autowired
    public EngineerServiceImplementation(EngineerRepositorySDR engineerRepository, CarRepositorySDR carRepository, TradeMarkRepositorySDR tradeMarkRepository) {
        this.engineerRepository = engineerRepository;
        this.carRepository = carRepository;
        this.tradeMarkRepository = tradeMarkRepository;
    }

    /**
     * Adding engineer with existent car (contribution)
     **/
    @Override
    public void addEngineer(Engineer engineer) {
        boolean isMatchEngineer = engineerRepository.findAll().stream().anyMatch(engineer1 -> engineer.getName().equalsIgnoreCase(engineer1.getName()));
        if (isMatchEngineer) {
            throw new EntityAlreadyExistsException(engineer.getName() + " already exists.");
        }
        engineerRepository.save(engineer);
    }

    /**
     * Adding engineer with a new car (contribution) Image required for new car
     **/
    @Override
    public void addEngineerWithNewCar(Engineer engineer, Car car, MultipartFile file) throws IOException {
        boolean isMatchEngineer = engineerRepository.findAll().stream().anyMatch(engineer1 -> engineer.getName().equalsIgnoreCase(engineer1.getName()));
        boolean isMatchCar = carRepository.findAll().stream().anyMatch(car1 -> car.getModel().equalsIgnoreCase(car1.getModel()));
        ServiceChecksUtil.checkIfAnyExists(engineer, car, isMatchEngineer, isMatchCar); //checks if any of the two entities already exist (based on model and name)
        ServiceChecksUtil.checkForTradeMark(car, carRepository, tradeMarkRepository);
        FileUploadUtil.addImageToCar(car, file);
        engineer.addCar(car);
        car.addEngineer(engineer);
        engineerRepository.save(engineer);
    }

    @Override
    public void deleteEngineer(Engineer engineer) {
        engineer.getCars().forEach(car -> car.getEngineers().remove(engineer));
        engineerRepository.delete(engineer);
    }

    @Override
    public List<Engineer> getAllEngineers() {
        return engineerRepository.findAll();
    }

    @Override
    public Engineer findById(int id) {
        return engineerRepository.findById(id).orElseThrow();
    }

    @Override
    public void update(Engineer engineer) {
        engineerRepository.save(engineer);
    }

    @Override
    public List<Engineer> findByNameContains(String name) {
        return engineerRepository.findByNameContainsIgnoreCase(name);
    }

    @Override
    public List<Engineer> findByTenureIsGreaterThanEqual(int tenure) {
        return engineerRepository.findByTenureIsGreaterThanEqual(tenure);
    }

    @Override
    public List<Engineer> findByTenureIsLessThanEqual(int tenure) {
        return engineerRepository.findByTenureIsLessThanEqual(tenure);
    }

    @Override
    public List<Engineer> findByTenure(int tenure) {
        return engineerRepository.findByTenure(tenure);
    }

}

