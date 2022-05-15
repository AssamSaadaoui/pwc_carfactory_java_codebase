package be.kdg.java2.carfactory_application.service;

import be.kdg.java2.carfactory_application.domain.factory.Contribution;
import be.kdg.java2.carfactory_application.repository.ContributionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ContributionServiceImplementation implements ContributionService {

    private final ContributionRepository contributionRepository;

    public ContributionServiceImplementation(ContributionRepository contributionRepository) {
        this.contributionRepository = contributionRepository;
    }


    @Override
    public void deleteContributionByEngineerId(int enId) {
        contributionRepository.deleteContributionByEngineerId(enId);
    }

    @Override
    public void deleteContributionByCarId(int carId) {
        contributionRepository.deleteContributionByCarId(carId);
    }

    @Override
    public void addContribution(Contribution c) {
        contributionRepository.save(c);
    }

    @Override
    public Contribution findContributionByEngIdandCarId(int i, int i1) {
        return contributionRepository.findContributionByEngineerIdAndCarId(i, i1);
    }

    @Override
    public Contribution findById(int i) {
        return contributionRepository.findById(i).orElse(null);
    }

}
