package be.kdg.java2.carfactory_application.service;

import be.kdg.java2.carfactory_application.domain.factory.Contribution;

public interface ContributionService {
    void deleteContributionByEngineerId(int enId);

    void deleteContributionByCarId(int carId);

    void addContribution(Contribution c);
}
