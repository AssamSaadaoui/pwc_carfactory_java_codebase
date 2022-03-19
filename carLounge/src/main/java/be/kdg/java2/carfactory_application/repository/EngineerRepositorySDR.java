package be.kdg.java2.carfactory_application.repository;

import be.kdg.java2.carfactory_application.domain.factory.Car;
import be.kdg.java2.carfactory_application.domain.factory.Engineer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

//@Profile("SDR")
public interface EngineerRepositorySDR extends JpaRepository<Engineer, Integer> {

    //    ?1 corresponds to first parameter (name), ?2 => second parameter (nationality)
    List<Engineer> findByNameContainsIgnoreCase(String name);

    List<Engineer> findByTenureIsGreaterThanEqual(int tenure);

    List<Engineer> findByTenureIsLessThanEqual(int tenure);

    List<Engineer> findByTenure(int tenure);


    @Override
    @Query("select e from Engineer e left join fetch e.author")
    List<Engineer> findAll();

    @Query("select e from Engineer e " +
            "left join fetch e.author " +
            "left join fetch e.contributions contribution " +
            "left join fetch contribution.car workedOnCar " +
            "left join workedOnCar.tradeMark where e.id=:enId")
    Optional<Engineer> findWithContributionsAndAuthor(int enId);
}

