package be.kdg.java2.carfactory_application.util;

import be.kdg.java2.carfactory_application.domain.Car;
import be.kdg.java2.carfactory_application.domain.Engineer;
import be.kdg.java2.carfactory_application.domain.TradeMark;
import be.kdg.java2.carfactory_application.exceptions.EntityAlreadyExistsException;
import be.kdg.java2.carfactory_application.repository.CarRepositorySDR;
import be.kdg.java2.carfactory_application.repository.TradeMarkRepositorySDR;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.function.Predicate;

public class ServiceChecksUtil {

    /**
     * With this method i do check for the existence of both entities, an exception is thrown if invalid
     **/
    public static void checkIfAnyExists(Engineer engineer, Car car, Boolean isMatchEngineer, Boolean isMatchCar) {
        if (isMatchEngineer) {
            throw new EntityAlreadyExistsException(engineer.getName() + " already exists.");
        }
        if (isMatchCar) {
            throw new EntityAlreadyExistsException(car.getModel() + " already exists.");
        }
    }

    public static void checkForTradeMark(Car car, CarRepositorySDR carRepository, TradeMarkRepositorySDR tradeMarkRepository) {
        boolean isTdExists = carRepository.findAll().stream().anyMatch(car1 -> car.getTradeMark().getTitle().equalsIgnoreCase(car1.getTradeMark().getTitle()));
        if (isTdExists) {
            Predicate<TradeMark> filterCond = tradeMark -> tradeMark.getTitle().equalsIgnoreCase(car.getTradeMark().getTitle());
            TradeMark alreadyTradeMark = tradeMarkRepository.findAll().stream().filter(filterCond).findFirst().orElseThrow();
            car.setTradeMark(alreadyTradeMark);
        }

    }
}
