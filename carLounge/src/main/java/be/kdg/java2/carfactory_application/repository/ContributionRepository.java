package be.kdg.java2.carfactory_application.repository;

import be.kdg.java2.carfactory_application.domain.Contribution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContributionRepository extends JpaRepository<Contribution, Integer> {
    void deleteByCarIdAndEngineerId(int carId, int enId);
}
