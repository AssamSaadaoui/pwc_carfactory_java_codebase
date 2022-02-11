package be.kdg.java2.carfactory_application.repository;

import be.kdg.java2.carfactory_application.domain.Car;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//@Profile("SDR")
public interface CarRepositorySDR extends JpaRepository<Car, Integer> {


    List<Car> findByModelContainsIgnoreCase(String model);

    @Query("select c from Car c order by c.price ASC")
    List<Car> findByOrderByPriceAsc();

    @Query("select c from Car c order by c.price DESC")
    List<Car> findByOrderByPriceDesc();


}
