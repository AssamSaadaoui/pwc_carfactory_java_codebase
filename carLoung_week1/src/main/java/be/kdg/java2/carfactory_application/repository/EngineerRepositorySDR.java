package be.kdg.java2.carfactory_application.repository;

import be.kdg.java2.carfactory_application.domain.Car;
import be.kdg.java2.carfactory_application.domain.Engineer;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//@Profile("SDR")
public interface EngineerRepositorySDR extends JpaRepository<Engineer, Integer> {

    //    ?1 corresponds to first parameter (name), ?2 => second parameter (nationality)
    List<Engineer> findByNameContainsIgnoreCase(String name);

    List<Engineer> findByTenureIsGreaterThanEqual(int tenure);

    List<Engineer> findByTenureIsLessThanEqual(int tenure);

    List<Engineer> findByTenure(int tenure);
}

