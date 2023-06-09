package be.kdg.java2.carfactory_application.repository;

import be.kdg.java2.carfactory_application.domain.factory.Contribution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContributionRepository extends JpaRepository<Contribution, Integer> {
    void deleteContributionByEngineerId(int enId);

    void deleteContributionByCarId(int carId);

    Contribution findContributionByEngineerIdAndCarId(int engId, int carId);
}
