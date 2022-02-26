package be.kdg.java2.carfactory_application.service;


import be.kdg.java2.carfactory_application.domain.Car;
import be.kdg.java2.carfactory_application.domain.Engineer;
import be.kdg.java2.carfactory_application.exception.EntityAlreadyExistsException;
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
public class CarServiceImplementation implements CarService {

    private final CarRepositorySDR carRepository;
    private final EngineerRepositorySDR engineerRepository;
    private final TradeMarkRepositorySDR tradeMarkRepository;

    @Autowired
    public CarServiceImplementation(CarRepositorySDR carRepository, EngineerRepositorySDR engineerRepository, TradeMarkRepositorySDR tradeMarkRepository) {
        this.carRepository = carRepository;
        this.engineerRepository = engineerRepository;
        this.tradeMarkRepository = tradeMarkRepository;
    }

    @Override
    public void addCar(Car car, MultipartFile file) throws IOException {
        boolean isMatch = carRepository.findAll().stream().anyMatch(car1 -> car.getModel().equalsIgnoreCase(car1.getModel()));
        if (isMatch) {
            throw new EntityAlreadyExistsException("Model: " + car.getModel() + " already exists.");
        }
        ServiceChecksUtil.checkForTradeMark(car, carRepository, tradeMarkRepository);
        FileUploadUtil.addImageToCar(car, file);
        carRepository.save(car);
    }

    @Override
    public void update(Car car, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            FileUploadUtil.addImageToCar(car, file);
        }
        carRepository.save(car);
    }

    @Override
    public void deleteCar(Car car) {
        carRepository.delete(car);
    }

    @Override
    public Car findById(int id) {
        return carRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public List<Car> findByModel(String model) {
        return carRepository.findByModelContainsIgnoreCase(model);
    }

    @Override
    public List<Car> orderByPriceAsc() {
        return carRepository.findByOrderByPriceAsc();
    }

    @Override
    public List<Car> orderByPriceDesc() {
        return carRepository.findByOrderByPriceDesc();
    }

    @Override
    public void update(Car contribution) {
        carRepository.save(contribution);
    }

    @Override
    public void addContributorToCar(Engineer newEngineer, int carId) {
        carRepository.findById(carId).orElseThrow().addEngineer(newEngineer);
    }

    @Override
    public void removeContributorFromCar(Car contribution, Engineer contributor) {
        contribution.removeEngineer(contributor);
        carRepository.save(contribution);// Update will cascade
    }

}
