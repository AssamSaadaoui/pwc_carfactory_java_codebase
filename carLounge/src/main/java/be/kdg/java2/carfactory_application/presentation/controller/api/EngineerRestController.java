package be.kdg.java2.carfactory_application.presentation.controller.api;

import be.kdg.java2.carfactory_application.presentation.controller.mvc.CarController;
import be.kdg.java2.carfactory_application.presentation.controller.api.dto.EngineerDTO;
import be.kdg.java2.carfactory_application.repository.ContributionRepository;
import be.kdg.java2.carfactory_application.service.CarService;
import be.kdg.java2.carfactory_application.service.ContributionService;
import be.kdg.java2.carfactory_application.service.EngineerService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/engineers")
public class EngineerRestController {
    private static final Logger logger = LoggerFactory.getLogger(CarController.class);

    private final ModelMapper modelMapper;

    private final EngineerService engineerService;
    private final CarService carService;
    private final ContributionService contributionService;

    public EngineerRestController(EngineerService engineerService,
                                  CarService carService,
                                  ModelMapper modelMapper,
                                  ContributionService contributionService) {
        this.engineerService = engineerService;
        this.carService = carService;
        this.modelMapper = modelMapper;
        this.contributionService = contributionService;
    }

    @GetMapping
    ResponseEntity<List<EngineerDTO>> searchEngineers(@RequestParam String lookup) {
        logger.debug("Looking up " + lookup);
        var engineers = engineerService.findByNameContains(lookup);
        if (engineers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        var engineerDTOS = engineers
                .stream()
                .map(engineer -> modelMapper.map(engineer, EngineerDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(engineerDTOS, HttpStatus.OK);
    }

    @DeleteMapping("/{enId}/cars/{carId}")
    public ResponseEntity<Void> deleteContribution(@PathVariable int enId, @PathVariable int carId) {
        logger.debug("Deleting relation between car and engineer");
        var contributor = engineerService.findById(enId);
        var contribution = carService.findById(carId);
        if (contributor == null || contribution == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //      Deleting relationship
        contributionService.deleteContributionByCarId(carId);
//        carService.removeContributorFromCar(contribution, contributor);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
