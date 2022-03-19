package be.kdg.java2.carfactory_application.repository;

import be.kdg.java2.carfactory_application.domain.factory.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//@Profile("SDR")
public interface CarRepositorySDR extends JpaRepository<Car, Integer> {
    //    With author as well for details page to validate authorization

    @Query("select c from Car c left join fetch c.author " +
            "left join fetch c.tradeMark " +
            "left join fetch c.contributions contribution " +
            "left join fetch contribution.engineer where c.id=:carId")
    Car findCarWithTradeMarkAndContributionsAndAuthor(int carId);

    @Override
    @Query("select c from Car c left join fetch c.author")
    List<Car> findAll();

    List<Car> findByModelContainsIgnoreCase(String model);

    @Query("select c from Car c order by c.price ASC")
    List<Car> findByOrderByPriceAsc();

    @Query("select c from Car c order by c.price DESC")
    List<Car> findByOrderByPriceDesc();


}
