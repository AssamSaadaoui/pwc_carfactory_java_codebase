package be.kdg.java2.carfactory_application.service;

import be.kdg.java2.carfactory_application.domain.factory.Car;
import be.kdg.java2.carfactory_application.domain.factory.Engineer;
import be.kdg.java2.carfactory_application.presentation.controller.api.dto.SimpleCarDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CarService {


    void addCar(Car car, MultipartFile file) throws IOException;

    List<Car> getAllCars();

    void update(Car car, MultipartFile multipartFile) throws IOException;

    void deleteCar(Car car);

    Car findById(int id);

    Car findByModel(String model);

    List<Car> findAllByModel(String model);

    List<Car> orderByPriceAsc();

    List<Car> orderByPriceDesc();

    void update(Car contribution);

    void addContributorToCar(Engineer newEngineer, int carId);

    void removeContributorFromCar(Car contribution, Engineer contributor);

    Car findCarWithTradeMarkAndContributionsAndAuthorById(int carId);

    Car findCarWithTradeMarkAndContributionsById(int carId);

    Car findCarWithTradeMarkById(int carId);

    void saveCarWithPartialUpdate(Car car);

//    List<Car> getCarsOnModelOrTradeMark(String model, String tradeMark);
}
