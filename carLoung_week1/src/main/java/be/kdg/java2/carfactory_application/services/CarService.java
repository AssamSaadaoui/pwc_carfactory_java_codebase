package be.kdg.java2.carfactory_application.services;

import be.kdg.java2.carfactory_application.domain.Car;
import be.kdg.java2.carfactory_application.domain.Engineer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CarService {


    void addCar(Car car, MultipartFile file) throws IOException;

    List<Car> getAllCars();

    void update(Car car, MultipartFile multipartFile) throws IOException;

    void deleteCar(Car car);

    Car findById(int id);

    List<Car> findByModel(String model);

    List<Car> orderByPriceAsc();

    List<Car> orderByPriceDesc();

    void update(Car contribution);

    void addContributorToCar(Engineer newEngineer, int carId);

    void removeContributorFromCar(Car contribution, Engineer contributor);


//    List<Car> getCarsOnModelOrTradeMark(String model, String tradeMark);
}
